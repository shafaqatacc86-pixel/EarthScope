package com.example.globe

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlin.concurrent.thread
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Custom Original Cinematic Space Orchestral Strings Synthesizer.
 * Inspired by the user's sample style (slow, expressive, emotional bowed strings & deep cello pads
 * with lush cathedral reverb and smooth legato transitions), composed as a 100% original piece
 * that plays exclusively when globe rotation and sound are enabled.
 */
class CosmicSoundEngine {

    @Volatile
    private var isPlaying = false
    private var audioThread: Thread? = null
    private var currentTrack: AudioTrack? = null

    private data class OrchestralChord(
        val bassFreq: Double,
        val padFreq1: Double,
        val padFreq2: Double,
        val leadStartFreq: Double,
        val leadEndFreq: Double,
        val durationSec: Double
    )

    fun setPlaying(shouldPlay: Boolean) {
        if (shouldPlay == isPlaying) return
        if (shouldPlay) {
            startSound()
        } else {
            stopSound()
        }
    }

    /**
     * Synthesizes a rich bowed string ensemble wave (violins/violas/cellos)
     * using detuned harmonic partials and expressive vibrato.
     */
    private fun bowedStringEnsemble(phase1: Double, phase2: Double, phase3: Double): Double {
        // Rich bowed string harmonic series (1st through 6th harmonics with warm rolloff)
        val voice1 = sin(phase1) +
                0.52 * sin(2.0 * phase1) +
                0.33 * sin(3.0 * phase1) +
                0.20 * sin(4.0 * phase1) +
                0.12 * sin(5.0 * phase1) +
                0.07 * sin(6.0 * phase1)

        val voice2 = sin(phase2) +
                0.48 * sin(2.0 * phase2) +
                0.30 * sin(3.0 * phase2) +
                0.16 * sin(4.0 * phase2)

        val voice3 = sin(phase3) +
                0.45 * sin(2.0 * phase3) +
                0.25 * sin(3.0 * phase3)

        return (voice1 * 0.45 + voice2 * 0.30 + voice3 * 0.25) * 0.52
    }

    private fun startSound() {
        if (isPlaying) return
        isPlaying = true

        audioThread = thread(start = true, isDaemon = true, name = "CosmicOrchestraThread") {
            val sampleRate = 24000
            val minBufSize = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            ).coerceAtLeast(4096)

            val track = try {
                AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(minBufSize)
                    .setTransferMode(AudioTrack.MODE_STREAM)
                    .build()
            } catch (e: Throwable) {
                isPlaying = false
                return@thread
            }

            currentTrack = track
            try {
                track.play()
            } catch (e: Throwable) {
                isPlaying = false
                return@thread
            }

            // Original 8-movement cinematic minor-key orchestral string progression
            // (Evokes deep emotion, slow celestial grandeur, and expressive solo cello/viola gliding)
            val progression = arrayOf(
                // 1. C minor swell — rising fifth to minor sixth
                OrchestralChord(65.41, 130.81, 196.00, 293.66, 311.13, 4.5),
                // 2. Ab Major 7 — warm soaring string sustain
                OrchestralChord(103.83, 155.56, 207.65, 311.13, 392.00, 4.5),
                // 3. Fm9 — deep emotional descent
                OrchestralChord(87.31, 130.81, 174.61, 349.23, 311.13, 4.5),
                // 4. G7sus4 -> G minor/major tension (cinematic dramatic resolution)
                OrchestralChord(98.00, 146.83, 196.00, 293.66, 246.94, 4.5),
                // 5. Cm / Eb — noble ascending string theme
                OrchestralChord(77.78, 130.81, 196.00, 261.63, 349.23, 4.5),
                // 6. Db Major 7 (Neapolitan atmospheric color, very cinematic)
                OrchestralChord(69.30, 138.59, 207.65, 349.23, 311.13, 4.5),
                // 7. Bbm7 — yearning nostalgic cello & viola melody
                OrchestralChord(116.54, 138.59, 233.08, 277.18, 261.63, 4.5),
                // 8. Gsus -> Cm — peaceful solemn cadence
                OrchestralChord(98.00, 146.83, 185.00, 293.66, 261.63, 4.5)
            )

            val chunkSamples = 1024
            val buffer = ShortArray(chunkSamples)

            // Multi-tap cathedral reverb delay lines for lush cinematic hall acoustics
            val delayLen1 = (sampleRate * 0.19).toInt()
            val delayLen2 = (sampleRate * 0.33).toInt()
            val delayLen3 = (sampleRate * 0.51).toInt()
            val reverbBuf1 = DoubleArray(delayLen1)
            val reverbBuf2 = DoubleArray(delayLen2)
            val reverbBuf3 = DoubleArray(delayLen3)
            var revIdx1 = 0
            var revIdx2 = 0
            var revIdx3 = 0

            // 2-pole warm low-pass filter state to emulate wooden string body resonance
            var lpState1 = 0.0
            var lpState2 = 0.0

            // Continuous phase accumulators to prevent any clicks between chords
            var bassPhase = 0.0
            var pad1PhaseA = 0.0
            var pad1PhaseB = 0.0
            var pad1PhaseC = 0.0
            var pad2PhaseA = 0.0
            var pad2PhaseB = 0.0
            var pad2PhaseC = 0.0
            var leadPhaseA = 0.0
            var leadPhaseB = 0.0
            var leadPhaseC = 0.0

            var chordIdx = 0
            var sampleInChord = 0L
            var globalSample = 0L

            try {
                while (isPlaying) {
                    val chord = progression[chordIdx]
                    val chordTotalSamples = (chord.durationSec * sampleRate).toLong()

                    for (i in 0 until chunkSamples) {
                        val t = globalSample.toDouble() / sampleRate
                        val progress = (sampleInChord.toDouble() / chordTotalSamples).coerceIn(0.0, 1.0)

                        // Smooth bow swell envelope per chord (gentle attack, full sustain, smooth crossfade)
                        val bowSwell = when {
                            progress < 0.18 -> 0.55 + 0.45 * sin((progress / 0.18) * (PI / 2.0))
                            progress > 0.82 -> 0.55 + 0.45 * cos(((progress - 0.82) / 0.18) * (PI / 2.0))
                            else -> 1.0
                        }

                        // Expressive legato glide between the two melody notes inside each movement
                        val glideCurve = when {
                            progress < 0.35 -> 0.0
                            progress < 0.65 -> 0.5 - 0.5 * cos(((progress - 0.35) / 0.30) * PI)
                            else -> 1.0
                        }
                        val currentLeadFreq = chord.leadStartFreq + (chord.leadEndFreq - chord.leadStartFreq) * glideCurve

                        // Natural human string vibrato (5.4 Hz with gentle depth)
                        val vibrato = 1.0 + 0.0042 * sin(2.0 * PI * 5.4 * t)
                        val slowDrift = 1.0 + 0.0018 * sin(2.0 * PI * 0.45 * t)

                        // Advance phases smoothly
                        val twoPiOverSr = 2.0 * PI / sampleRate
                        bassPhase += twoPiOverSr * chord.bassFreq
                        pad1PhaseA += twoPiOverSr * (chord.padFreq1 * 0.997 * slowDrift)
                        pad1PhaseB += twoPiOverSr * (chord.padFreq1 * 1.000)
                        pad1PhaseC += twoPiOverSr * (chord.padFreq1 * 1.003 / slowDrift)

                        pad2PhaseA += twoPiOverSr * (chord.padFreq2 * 0.996 * slowDrift)
                        pad2PhaseB += twoPiOverSr * (chord.padFreq2 * 1.000)
                        pad2PhaseC += twoPiOverSr * (chord.padFreq2 * 1.004 / slowDrift)

                        leadPhaseA += twoPiOverSr * (currentLeadFreq * vibrato * 0.998)
                        leadPhaseB += twoPiOverSr * (currentLeadFreq * vibrato)
                        leadPhaseC += twoPiOverSr * (currentLeadFreq * vibrato * 1.002)

                        // 1. Deep Double Bass & Cello Pedals
                        val bassSignal = (sin(bassPhase) + 0.45 * sin(2.0 * bassPhase) + 0.20 * sin(3.0 * bassPhase)) * 0.26

                        // 2. Warm Viola & Second Violin Section Harmony Pads
                        val pad1Signal = bowedStringEnsemble(pad1PhaseA, pad1PhaseB, pad1PhaseC) * 0.28
                        val pad2Signal = bowedStringEnsemble(pad2PhaseA, pad2PhaseB, pad2PhaseC) * 0.24

                        // 3. Expressive Solo Violin / Cello Legato Melody Line
                        val leadSignal = bowedStringEnsemble(leadPhaseA, leadPhaseB, leadPhaseC) * 0.42 * bowSwell

                        val rawMix = (bassSignal + pad1Signal + pad2Signal + leadSignal) * bowSwell

                        // Warm wooden string body low-pass filter (removes harsh digital highs)
                        lpState1 += 0.16 * (rawMix - lpState1)
                        lpState2 += 0.22 * (lpState1 - lpState2)
                        val warmStrings = lpState2

                        // Lush 3-tap cathedral reverb
                        val rev1 = reverbBuf1[revIdx1]
                        val rev2 = reverbBuf2[revIdx2]
                        val rev3 = reverbBuf3[revIdx3]
                        val reverbOut = 0.45 * rev1 + 0.35 * rev2 + 0.25 * rev3

                        reverbBuf1[revIdx1] = warmStrings + rev1 * 0.42
                        reverbBuf2[revIdx2] = warmStrings + rev2 * 0.38
                        reverbBuf3[revIdx3] = warmStrings + rev3 * 0.32

                        revIdx1 = (revIdx1 + 1) % delayLen1
                        revIdx2 = (revIdx2 + 1) % delayLen2
                        revIdx3 = (revIdx3 + 1) % delayLen3

                        // Final master fade-in over first 1.2 seconds so starting rotation feels silky smooth
                        val masterFadeIn = (t / 1.2).coerceIn(0.0, 1.0)
                        val finalSample = ((warmStrings * 0.68 + reverbOut * 0.42) * 0.42 * masterFadeIn)
                            .coerceIn(-0.95, 0.95)

                        buffer[i] = (finalSample * Short.MAX_VALUE).toInt().toShort()

                        sampleInChord++
                        globalSample++
                        if (sampleInChord >= chordTotalSamples) {
                            sampleInChord = 0L
                            chordIdx = (chordIdx + 1) % progression.size
                        }
                    }

                    if (!isPlaying) break
                    track.write(buffer, 0, chunkSamples)
                }
            } catch (_: Throwable) {
                // Ignore audio interruption on shutdown
            } finally {
                try {
                    track.stop()
                } catch (_: Throwable) {}
                try {
                    track.release()
                } catch (_: Throwable) {}
                if (currentTrack === track) {
                    currentTrack = null
                }
            }
        }
    }

    fun stopSound() {
        isPlaying = false
        try {
            currentTrack?.pause()
            currentTrack?.flush()
        } catch (_: Throwable) {}
        audioThread?.interrupt()
        audioThread = null
    }
}
