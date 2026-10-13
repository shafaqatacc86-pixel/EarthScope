package com.example.data.model

import kotlin.math.cos

object AdditionalWorldCountries {

    /**
     * Generates a clean spherical territory boundary polygon around a country's center
     * scaled by its land area (km²) so every country on Earth has a visible, clickable,
     * and highlighted polygon border on the 3D globe.
     */
    private fun approxPolygon(lat: Double, lon: Double, areaKm2: Double): List<GeoPoint> {
        val rawDeg = kotlin.math.sqrt(areaKm2.coerceAtLeast(1500.0)) / 111.0 * 0.52
        val halfLat = rawDeg.coerceIn(0.45, 5.2)
        val cosLat = cos(Math.toRadians(lat)).coerceAtLeast(0.25)
        val halfLon = (halfLat / cosLat).coerceIn(0.55, 7.5)

        return listOf(
            GeoPoint(lat + halfLat, lon - halfLon * 0.65),
            GeoPoint(lat + halfLat * 0.85, lon + halfLon * 0.65),
            GeoPoint(lat + halfLat * 0.15, lon + halfLon),
            GeoPoint(lat - halfLat * 0.75, lon + halfLon * 0.75),
            GeoPoint(lat - halfLat, lon + halfLon * 0.1),
            GeoPoint(lat - halfLat * 0.8, lon - halfLon * 0.75),
            GeoPoint(lat - halfLat * 0.1, lon - halfLon),
            GeoPoint(lat + halfLat, lon - halfLon * 0.65)
        )
    }

    internal fun createCountry(
        id: String,
        name: String,
        nativeName: String,
        capital: String,
        continent: String,
        region: String,
        pop: Long,
        area: Double,
        currency: String,
        langs: List<String>,
        flag: String,
        cLat: Double,
        cLon: Double,
        capLat: Double,
        capLon: Double,
        tz: Double,
        desc: String,
        customBoundary: List<GeoPoint>? = null
    ): CountryInfo = c(
        id, name, nativeName, capital, continent, region, pop, area, currency,
        langs, flag, cLat, cLon, capLat, capLon, tz, desc, customBoundary
    )

    private fun c(
        id: String,
        name: String,
        nativeName: String,
        capital: String,
        continent: String,
        region: String,
        pop: Long,
        area: Double,
        currency: String,
        langs: List<String>,
        flag: String,
        cLat: Double,
        cLon: Double,
        capLat: Double,
        capLon: Double,
        tz: Double,
        desc: String,
        customBoundary: List<GeoPoint>? = null
    ): CountryInfo {
        return CountryInfo(
            id = id,
            name = name,
            nativeName = nativeName,
            capital = capital,
            continent = continent,
            region = region,
            population = pop,
            areaKm2 = area,
            currency = currency,
            languages = langs,
            flagEmoji = flag,
            centerLat = cLat,
            centerLon = cLon,
            capitalLat = capLat,
            capitalLon = capLon,
            timeZoneOffsetHours = tz,
            description = desc,
            boundary = customBoundary ?: approxPolygon(cLat, cLon, area)
        )
    }

    val list: List<CountryInfo> by lazy {
        listOf(
            // ================= SOUTH, CENTRAL & WEST ASIA / MIDDLE EAST =================
            c("BD", "Bangladesh", "বাংলাদেশ", "Dhaka", "Asia", "South Asia", 172954319L, 148460.0, "Bangladeshi Taka (BDT)", listOf("Bengali"), "🇧🇩", 23.6850, 90.3563, 23.8103, 90.4125, 6.0, "Lush riverine country in South Asia on the Bay of Bengal, home to the Sundarbans mangrove forest."),
            c("LK", "Sri Lanka", "ශ්‍රී ලංකාව", "Colombo", "Asia", "South Asia", 21893579L, 65610.0, "Sri Lankan Rupee (LKR)", listOf("Sinhala", "Tamil"), "🇱🇰", 7.8731, 80.7718, 6.9271, 79.8612, 5.5, "Tropical island nation south of India known for ancient Buddhist ruins, tea plantations, and sandy beaches."),
            c("NP", "Nepal", "नेपाल", "Kathmandu", "Asia", "South Asia", 30896590L, 147516.0, "Nepalese Rupee (NPR)", listOf("Nepali"), "🇳🇵", 28.3949, 84.1240, 27.7172, 85.3240, 5.75, "Himalayan country home to Mount Everest, the highest peak on Earth, and rich mountain trekking heritage."),
            c("BT", "Bhutan", "འབྲུག་ཡུལ་", "Thimphu", "Asia", "South Asia", 787424L, 38394.0, "Bhutanese Ngultrum (BTN)", listOf("Dzongkha"), "🇧🇹", 27.5142, 90.4336, 27.4728, 89.6393, 6.0, "Buddhist kingdom on the Himalayas' eastern edge, known for monasteries, fortresses, and Gross National Happiness."),
            c("MV", "Maldives", "ދިވެހިރާއްޖެ", "Malé", "Asia", "South Asia", 521021L, 300.0, "Maldivian Rufiyaa (MVR)", listOf("Dhivehi"), "🇲🇻", 3.2028, 73.2207, 4.1755, 73.5093, 5.0, "Indian Ocean archipelagic nation of coral atolls, turquoise lagoons, and coral reefs."),
            c("KZ", "Kazakhstan", "Қазақстан", "Astana", "Asia", "Central Asia", 19606633L, 2724900.0, "Kazakhstani Tenge (KZT)", listOf("Kazakh", "Russian"), "🇰🇿", 48.0196, 66.9237, 51.1694, 71.4491, 5.0, "World's largest landlocked country stretching from the Caspian Sea to the Altai Mountains."),
            c("UZ", "Uzbekistan", "Oʻzbekiston", "Tashkent", "Asia", "Central Asia", 36412350L, 447400.0, "Uzbekistani Som (UZS)", listOf("Uzbek"), "🇺🇿", 41.3775, 64.5853, 41.2995, 69.2401, 5.0, "Heart of the ancient Silk Road, famed for Samarkand, Bukhara, and Khiva architectural wonders."),
            c("TM", "Turkmenistan", "Türkmenistan", "Ashgabat", "Asia", "Central Asia", 6516100L, 488100.0, "Turkmen New Manat (TMT)", listOf("Turkmen"), "🇹🇲", 38.9697, 59.5563, 37.9601, 58.3261, 5.0, "Central Asian country bordered by the Caspian Sea and covered largely by the Karakum Desert."),
            c("TJ", "Tajikistan", "Тоҷикистон", "Dushanbe", "Asia", "Central Asia", 10143543L, 143100.0, "Tajikistani Somoni (TJS)", listOf("Tajik"), "🇹🇯", 38.8610, 71.2761, 38.5598, 68.7870, 5.0, "Mountainous Central Asian country famed for the Pamir Mountains and alpine lakes."),
            c("KG", "Kyrgyzstan", "Кыргызстан", "Bishkek", "Asia", "Central Asia", 7100000L, 199951.0, "Kyrgyzstani Som (KGS)", listOf("Kyrgyz", "Russian"), "🇰🇬", 41.2044, 74.7661, 42.8746, 74.5698, 6.0, "Central Asian nation defined by the Tien Shan mountain range and Lake Issyk-Kul."),
            c("IQ", "Iraq", "العراق", "Baghdad", "Asia", "Middle East", 45504560L, 438317.0, "Iraqi Dinar (IQD)", listOf("Arabic", "Kurdish"), "🇮🇶", 33.2232, 43.6793, 33.3152, 44.3661, 3.0, "Cradle of civilization in Mesopotamia along the Tigris and Euphrates rivers."),
            c("SY", "Syria", "سوريا", "Damascus", "Asia", "Middle East", 23227014L, 185180.0, "Syrian Pound (SYP)", listOf("Arabic"), "🇸🇾", 34.8021, 38.9968, 33.5138, 36.2765, 3.0, "Eastern Mediterranean nation home to Damascus and Aleppo, among the oldest continuously inhabited cities."),
            c("JO", "Jordan", "الأردن", "Amman", "Asia", "Middle East", 11337052L, 89342.0, "Jordanian Dinar (JOD)", listOf("Arabic"), "🇯🇴", 30.5852, 36.2384, 31.9454, 35.9284, 3.0, "Arab nation on the east bank of the Jordan River, famous for the rose-red ancient city of Petra and Wadi Rum."),
            c("LB", "Lebanon", "لبنان", "Beirut", "Asia", "Middle East", 5353930L, 10452.0, "Lebanese Pound (LBP)", listOf("Arabic", "French"), "🇱🇧", 33.8547, 35.8623, 33.8938, 35.5018, 2.0, "Levantine Mediterranean country known for ancient Roman ruins at Baalbek, cedar forests, and Beirut."),
            c("PS", "Palestine", "فلسطين", "Jerusalem / Ramallah", "Asia", "Middle East", 5483450L, 6020.0, "Jordanian Dinar / ILS", listOf("Arabic"), "🇵🇸", 31.9522, 35.2332, 31.7683, 35.2137, 2.0, "Historic land in the Levant region of West Asia, home to sacred religious landmarks in Jerusalem, Bethlehem, and Hebron."),
            c("IL", "Israel", "ישראל", "Tel Aviv / Jerusalem", "Asia", "Middle East", 9756700L, 20770.0, "New Shekel (ILS)", listOf("Hebrew", "Arabic"), "🇮🇱", 31.0461, 34.8516, 32.0853, 34.7818, 2.0, "Located on the southeastern shore of the Mediterranean Sea and the northern shore of the Red Sea."),
            c("KW", "Kuwait", "الكويت", "Kuwait City", "Asia", "Middle East", 4310108L, 17818.0, "Kuwaiti Dinar (KWD)", listOf("Arabic"), "🇰🇼", 29.3117, 47.4818, 29.3759, 47.9774, 3.0, "Arab country at the tip of the Persian Gulf, known for the iconic Kuwait Towers and rich oil reserves."),
            c("QA", "Qatar", "قطر", "Doha", "Asia", "Middle East", 2716391L, 11586.0, "Qatari Riyal (QAR)", listOf("Arabic"), "🇶🇦", 25.3548, 51.1839, 25.2854, 51.5310, 3.0, "Peninsular Arab country on the Persian Gulf with a futuristic Doha skyline and Museum of Islamic Art."),
            c("BH", "Bahrain", "البحرين", "Manama", "Asia", "Middle East", 1485509L, 786.0, "Bahraini Dinar (BHD)", listOf("Arabic"), "🇧🇭", 26.0667, 50.5577, 26.2285, 50.5860, 3.0, "Island kingdom in the Persian Gulf celebrated for ancient Dilmun civilization sites and pearl diving heritage."),
            c("OM", "Oman", "عُمان", "Muscat", "Asia", "Middle East", 4644384L, 309500.0, "Omani Rial (OMR)", listOf("Arabic"), "🇴🇲", 21.4735, 55.9754, 23.5880, 58.3829, 4.0, "Located on the southeastern coast of the Arabian Peninsula with dramatic mountains, wadis, and forts."),
            c("YE", "Yemen", "اليمن", "Sana'a", "Asia", "Middle East", 34449825L, 527968.0, "Yemeni Rial (YER)", listOf("Arabic"), "🇾🇪", 15.5527, 48.5164, 15.3694, 44.1910, 3.0, "Situated at the southern end of the Arabian Peninsula, known for Old Sana'a architecture and Socotra Island."),
            c("AZ", "Azerbaijan", "Azərbaycan", "Baku", "Asia", "Caucasus", 10112555L, 86600.0, "Azerbaijani Manat (AZN)", listOf("Azerbaijani"), "🇦🇿", 40.1431, 47.5769, 40.4093, 49.8671, 4.0, "Land of Fire straddling Eastern Europe and Western Asia along the Caspian Sea, famed for Baku's Flame Towers."),
            c("GE", "Georgia", "საქართველო", "Tbilisi", "Asia", "Caucasus", 3736400L, 69700.0, "Georgian Lari (GEL)", listOf("Georgian"), "🇬🇪", 42.3154, 43.3569, 41.7151, 44.8271, 4.0, "Intersection of Europe and Asia in the Caucasus Mountains with Black Sea beaches and historic Tbilisi."),
            c("AM", "Armenia", "Հայաստան", "Yerevan", "Asia", "Caucasus", 2777970L, 29743.0, "Armenian Dram (AMD)", listOf("Armenian"), "🇦🇲", 40.0691, 45.0382, 40.1792, 44.4991, 4.0, "Landlocked country in the South Caucasus highland region, home to Lake Sevan and ancient monasteries."),

            // ================= EAST & SOUTHEAST ASIA =================
            c("KR", "South Korea", "대한민국", "Seoul", "Asia", "East Asia", 51784059L, 100210.0, "South Korean Won (KRW)", listOf("Korean"), "🇰🇷", 35.9078, 127.7669, 37.5665, 126.9780, 9.0, "East Asian nation on the southern half of the Korean Peninsula, known for high-tech Seoul and ancient palaces."),
            c("KP", "North Korea", "조선민주주의인민공화국", "Pyongyang", "Asia", "East Asia", 26160821L, 120540.0, "North Korean Won (KPW)", listOf("Korean"), "🇰🇵", 40.3399, 127.5101, 39.0392, 125.7625, 9.0, "Occupies the northern portion of the Korean Peninsula between the Korea Bay and the Sea of Japan."),
            c("TW", "Taiwan", "臺灣", "Taipei", "Asia", "East Asia", 23894394L, 36193.0, "New Taiwan Dollar (TWD)", listOf("Mandarin"), "🇹🇼", 23.6978, 120.9605, 25.0330, 121.5654, 8.0, "East Asian island famed for Taipei 101, dramatic Taroko Gorge mountains, and semiconductor innovation."),
            c("MM", "Myanmar", "မြန်မာ", "Naypyidaw", "Asia", "Southeast Asia", 54577997L, 676578.0, "Myanmar Kyat (MMK)", listOf("Burmese"), "🇲🇲", 21.9162, 95.9560, 19.7633, 96.0785, 6.5, "Southeast Asian nation of more than 100 ethnic groups, famed for Bagan temples and Shwedagon Pagoda."),
            c("TH", "Thailand", "ประเทศไทย", "Bangkok", "Asia", "Southeast Asia", 71801279L, 513120.0, "Thai Baht (THB)", listOf("Thai"), "🇹🇭", 15.8700, 100.9925, 13.7563, 100.5018, 7.0, "Known for tropical beaches, opulent royal palaces, ornate shrines, and vibrant street life in Bangkok."),
            c("VN", "Vietnam", "Việt Nam", "Hanoi", "Asia", "Southeast Asia", 98858950L, 331212.0, "Vietnamese Dong (VND)", listOf("Vietnamese"), "🇻🇳", 14.0583, 108.2772, 21.0285, 105.8542, 7.0, "Southeast Asian country on the South China Sea known for Ha Long Bay, Mekong Delta, and bustling cities."),
            c("KH", "Cambodia", "កម្ពុជា", "Phnom Penh", "Asia", "Southeast Asia", 16944826L, 181035.0, "Cambodian Riel (KHR)", listOf("Khmer"), "🇰🇭", 12.5657, 104.9910, 11.5564, 104.9282, 7.0, "Southeast Asian nation whose landscape spans low-lying plains, the Mekong Delta, and Angkor Wat ruins."),
            c("LA", "Laos", "ສປປ ລາວ", "Vientiane", "Asia", "Southeast Asia", 7633779L, 236800.0, "Lao Kip (LAK)", listOf("Lao"), "🇱🇦", 19.8563, 102.4955, 17.9757, 102.6331, 7.0, "Mountainous landlocked Southeast Asian country traversed by the Mekong River and known for Luang Prabang."),
            c("SG", "Singapore", "Singapore", "Singapore", "Asia", "Southeast Asia", 5917600L, 734.0, "Singapore Dollar (SGD)", listOf("English", "Malay", "Mandarin", "Tamil"), "🇸🇬", 1.3521, 103.8198, 1.3521, 103.8198, 8.0, "Global financial hub and island city-state off southern Malaysia, famous for Marina Bay Sands and Gardens by the Bay."),
            c("PH", "Philippines", "Pilipinas", "Manila", "Asia", "Southeast Asia", 117337368L, 300000.0, "Philippine Peso (PHP)", listOf("Filipino", "English"), "🇵🇭", 12.8797, 121.7740, 14.5995, 120.9842, 8.0, "Archipelago of more than 7,000 islands in the western Pacific Ocean with emerald rice terraces and lagoons."),
            c("BN", "Brunei", "بروني", "Bandar Seri Begawan", "Asia", "Southeast Asia", 452524L, 5765.0, "Brunei Dollar (BND)", listOf("Malay"), "🇧🇳", 4.5353, 114.7277, 4.9031, 114.9398, 8.0, "Sultanate on the island of Borneo, known for biodiverse rainforests and the Omar Ali Saifuddien Mosque."),
            c("TL", "Timor-Leste", "Timor-Leste", "Dili", "Asia", "Southeast Asia", 1360596L, 14874.0, "US Dollar (USD)", listOf("Tetum", "Portuguese"), "🇹🇱", -8.8742, 125.7275, -8.5569, 125.5603, 9.0, "Southeast Asian nation occupying half the island of Timor, ringed by coral reefs and rugged mountains."),

            // ================= EUROPE =================
            c("SE", "Sweden", "Sverige", "Stockholm", "Europe", "Northern Europe", 10551707L, 450295.0, "Swedish Krona (SEK)", listOf("Swedish"), "🇸🇪", 60.1282, 18.6435, 59.3293, 18.0686, 1.0, "Scandinavian nation with thousands of coastal islands, inland lakes, boreal forests, and glaciated mountains."),
            c("FI", "Finland", "Suomi", "Helsinki", "Europe", "Northern Europe", 5563970L, 338424.0, "Euro (EUR)", listOf("Finnish", "Swedish"), "🇫🇮", 61.9241, 25.7482, 60.1699, 24.9384, 2.0, "Northern European nation known for Lapland Northern Lights, vast forests, and over 180,000 lakes."),
            c("DK", "Denmark", "Danmark", "Copenhagen", "Europe", "Northern Europe", 5932654L, 42933.0, "Danish Krone (DKK)", listOf("Danish"), "🇩🇰", 56.2639, 9.5018, 55.6761, 12.5683, 1.0, "Scandinavian country comprising the Jutland Peninsula and numerous islands, linked to Sweden via the Öresund Bridge."),
            c("IS", "Iceland", "Ísland", "Reykjavik", "Europe", "Northern Europe", 387758L, 103000.0, "Icelandic Króna (ISK)", listOf("Icelandic"), "🇮🇸", 64.9631, -19.0208, 64.1466, -21.9426, 0.0, "Nordic island nation defined by dramatic volcanic landscapes, geysers, hot springs, and massive glaciers."),
            c("IE", "Ireland", "Éire", "Dublin", "Europe", "Western Europe", 5194336L, 70273.0, "Euro (EUR)", listOf("Irish", "English"), "🇮🇪", 53.1424, -7.6921, 53.3498, -6.2603, 0.0, "The Emerald Isle in the North Atlantic, celebrated for lush green valleys, Cliffs of Moher, and historic castles."),
            c("PT", "Portugal", "Portugal", "Lisbon", "Europe", "Southern Europe", 10467366L, 92212.0, "Euro (EUR)", listOf("Portuguese"), "🇵🇹", 39.3999, -8.2245, 38.7223, -9.1393, 0.0, "Southern European country on the Iberian Peninsula bordering Spain, renowned for maritime history and Atlantic beaches."),
            c("NL", "Netherlands", "Nederland", "Amsterdam", "Europe", "Western Europe", 17879488L, 41850.0, "Euro (EUR)", listOf("Dutch"), "🇳🇱", 52.1326, 5.2913, 52.3676, 4.9041, 1.0, "Northwestern European country known for flat landscapes of canals, tulip fields, windmills, and cycling routes."),
            c("BE", "Belgium", "België / Belgique", "Brussels", "Europe", "Western Europe", 11686140L, 30528.0, "Euro (EUR)", listOf("Dutch", "French", "German"), "🇧🇪", 50.5039, 4.4699, 50.8503, 4.3517, 1.0, "Western European country known for medieval towns, Renaissance architecture, and headquarters of the EU."),
            c("CH", "Switzerland", "Schweiz / Suisse", "Bern", "Europe", "Central Europe", 8849852L, 41285.0, "Swiss Franc (CHF)", listOf("German", "French", "Italian"), "🇨🇭", 46.8182, 8.2275, 46.9480, 7.4474, 1.0, "Mountainous Central European country home to the high peaks of the Alps, pristine lakes, and neutrality."),
            c("AT", "Austria", "Österreich", "Vienna", "Europe", "Central Europe", 9104772L, 83879.0, "Euro (EUR)", listOf("German"), "🇦🇹", 47.5162, 14.5501, 48.2082, 16.3738, 1.0, "Alpine nation in Central Europe famed for imperial Vienna palaces, classical music legacy, and mountain villages."),
            c("PL", "Poland", "Polska", "Warsaw", "Europe", "Central Europe", 36753736L, 312696.0, "Polish Złoty (PLN)", listOf("Polish"), "🇵🇱", 51.9194, 19.1451, 52.2297, 21.0122, 1.0, "Central European nation on the Baltic Sea known for medieval architecture, Krakow's castle, and Tatra Mountains."),
            c("UA", "Ukraine", "Україна", "Kyiv", "Europe", "Eastern Europe", 36744634L, 603628.0, "Ukrainian Hryvnia (UAH)", listOf("Ukrainian"), "🇺🇦", 48.3794, 31.1656, 50.4501, 30.5234, 2.0, "Large Eastern European country known for Orthodox cathedrals in Kyiv, fertile plains, and Black Sea coastline."),
            c("BY", "Belarus", "Беларусь", "Minsk", "Europe", "Eastern Europe", 9200617L, 207600.0, "Belarusian Ruble (BYN)", listOf("Belarusian", "Russian"), "🇧🇾", 53.7098, 27.9534, 53.9006, 27.5590, 3.0, "Landlocked country in Eastern Europe known for primeval Białowieża Forest and historic Mir and Nesvizh castles."),
            c("CZ", "Czechia", "Česko", "Prague", "Europe", "Central Europe", 10827529L, 78871.0, "Czech Koruna (CZK)", listOf("Czech"), "🇨🇿", 49.8175, 15.4730, 50.0755, 14.4378, 1.0, "Central European country famed for ornate castles, Prague's Charles Bridge, and historic Bohemia."),
            c("SK", "Slovakia", "Slovensko", "Bratislava", "Europe", "Central Europe", 5428792L, 49035.0, "Euro (EUR)", listOf("Slovak"), "🇸🇰", 48.6690, 19.6990, 48.1486, 17.1077, 1.0, "Central European country known for dramatic High Tatras mountain peaks and medieval castles."),
            c("HU", "Hungary", "Magyarország", "Budapest", "Europe", "Central Europe", 9597085L, 93030.0, "Hungarian Forint (HUF)", listOf("Hungarian"), "🇭🇺", 47.1625, 19.5033, 47.4979, 19.0402, 1.0, "Landlocked Central European country bisected by the Danube River, famous for Budapest's thermal spas and Parliament."),
            c("RO", "Romania", "România", "Bucharest", "Europe", "Southeastern Europe", 19051562L, 238397.0, "Romanian Leu (RON)", listOf("Romanian"), "🇷🇴", 45.9432, 24.9668, 44.4268, 26.1025, 2.0, "Southeastern European country known for the forested region of Transylvania, Carpathian Mountains, and Danube Delta."),
            c("BG", "Bulgaria", "България", "Sofia", "Europe", "Southeastern Europe", 6447710L, 110994.0, "Bulgarian Lev (BGN)", listOf("Bulgarian"), "🇧🇬", 42.7339, 25.4858, 42.6977, 23.3219, 2.0, "Balkan nation with diverse terrain encompassing Black Sea coastline, Rila Mountains, and the Danube river."),
            c("GR", "Greece", "Ελλάδα", "Athens", "Europe", "Southern Europe", 10341277L, 131957.0, "Euro (EUR)", listOf("Greek"), "🇬🇷", 39.0742, 21.8243, 37.9838, 23.7275, 2.0, "Cradle of Western civilization in southeastern Europe with thousands of islands across the Aegean and Ionian seas."),
            c("RS", "Serbia", "Србија", "Belgrade", "Europe", "Balkans", 6664449L, 88361.0, "Serbian Dinar (RSD)", listOf("Serbian"), "🇷🇸", 44.0165, 21.0059, 44.7866, 20.4489, 1.0, "Southeast European country at the crossroads of the Pannonian Plain and the Balkans, centered on Belgrade."),
            c("HR", "Croatia", "Hrvatska", "Zagreb", "Europe", "Balkans", 3850894L, 56594.0, "Euro (EUR)", listOf("Croatian"), "🇭🇷", 45.1000, 15.2000, 45.8150, 15.9819, 1.0, "Crescent-shaped country on the Adriatic Sea famed for walled Dubrovnik, Plitvice Lakes, and Dalmatian islands."),
            c("BA", "Bosnia and Herzegovina", "Bosna i Hercegovina", "Sarajevo", "Europe", "Balkans", 3210847L, 51197.0, "Convertible Mark (BAM)", listOf("Bosnian", "Croatian", "Serbian"), "🇧🇦", 43.9159, 17.6791, 43.8563, 18.4131, 1.0, "Balkan country known for medieval villages, Dinaric Alps, Sarajevo heritage, and Mostar's Stari Most bridge."),
            c("AL", "Albania", "Shqipëria", "Tirana", "Europe", "Balkans", 2761785L, 28748.0, "Albanian Lek (ALL)", listOf("Albanian"), "🇦🇱", 41.1533, 20.1683, 41.3275, 19.8187, 1.0, "On Europe's Balkan Peninsula with Ionian and Adriatic coastlines, Albanian Alps, and ancient castles."),
            c("MK", "North Macedonia", "Северна Македонија", "Skopje", "Europe", "Balkans", 1832696L, 25713.0, "Macedonian Denar (MKD)", listOf("Macedonian", "Albanian"), "🇲🇰", 41.6086, 21.7453, 41.9981, 21.4254, 1.0, "Balkan nation known for ancient Lake Ohrid, mountains, and historic Skopje."),
            c("SI", "Slovenia", "Slovenija", "Ljubljana", "Europe", "Central Europe", 2116972L, 20273.0, "Euro (EUR)", listOf("Slovene"), "🇸🇮", 46.1512, 14.9955, 46.0569, 14.5058, 1.0, "Central European country known for Julian Alps, Lake Bled, and green forests."),
            c("LT", "Lithuania", "Lietuva", "Vilnius", "Europe", "Baltics", 2860002L, 65300.0, "Euro (EUR)", listOf("Lithuanian"), "🇱🇹", 55.1694, 23.8813, 54.6872, 25.2797, 2.0, "Southernmost of Europe's Baltic states, known for Vilnius's baroque Old Town and Curonian Spit dunes."),
            c("LV", "Latvia", "Latvija", "Riga", "Europe", "Baltics", 1883008L, 64589.0, "Euro (EUR)", listOf("Latvian"), "🇱🇻", 56.8796, 24.6032, 56.9496, 24.1052, 2.0, "Baltic Sea nation between Lithuania and Estonia, renowned for Art Nouveau architecture in Riga."),
            c("EE", "Estonia", "Eesti", "Tallinn", "Europe", "Baltics", 1365884L, 45227.0, "Euro (EUR)", listOf("Estonian"), "🇪🇪", 58.5953, 25.0136, 59.4370, 24.7536, 2.0, "Northern European Baltic country with medieval Tallinn Old Town, digital innovation, and over 2,000 islands."),
            c("MD", "Moldova", "Moldova", "Chișinău", "Europe", "Eastern Europe", 2512758L, 33846.0, "Moldovan Leu (MDL)", listOf("Romanian"), "🇲🇩", 47.4116, 28.3699, 47.0105, 28.8638, 2.0, "Eastern European country bordered by Romania and Ukraine, known for rolling hills and vineyards."),

            // ================= AFRICA =================
            c("MA", "Morocco", "المغرب", "Rabat", "Africa", "North Africa", 37840044L, 446550.0, "Moroccan Dirham (MAD)", listOf("Arabic", "Berber"), "🇲🇦", 31.7917, -7.0926, 34.0209, -6.8416, 1.0, "North African kingdom bordering the Atlantic Ocean and Mediterranean Sea, famed for Marrakech, Fez, and Atlas Mountains."),
            c("TN", "Tunisia", "تونس", "Tunis", "Africa", "North Africa", 12458223L, 163610.0, "Tunisian Dinar (TND)", listOf("Arabic"), "🇹🇳", 33.8869, 9.5375, 36.8065, 10.1815, 1.0, "North African country on the Mediterranean coast and Sahara desert, home to ancient Carthage ruins."),
            c("MR", "Mauritania", "موريتانيا", "Nouakchott", "Africa", "Northwest Africa", 4862989L, 1030700.0, "Mauritanian Ouguiya (MRU)", listOf("Arabic"), "🇲🇷", 21.0079, -10.9408, 18.0735, -15.9582, 0.0, "Bridge between the Arab Maghreb and western sub-Saharan Africa along the Atlantic coast."),
            c("ML", "Mali", "Mali", "Bamako", "Africa", "West Africa", 23293698L, 1240192.0, "West African CFA Franc (XOF)", listOf("French", "Bambara"), "🇲🇱", 17.5707, -3.9962, 12.6392, -8.0029, 0.0, "West African nation renowned for the historic trading city of Timbuktu and the Niger River."),
            c("NE", "Niger", "Niger", "Niamey", "Africa", "West Africa", 27202843L, 1267000.0, "West African CFA Franc (XOF)", listOf("French", "Hausa"), "🇳🇪", 17.6078, 8.0817, 13.5116, 2.1254, 1.0, "Landlocked West African country named after the Niger River, featuring the Aïr Mountains and Sahara dunes."),
            c("TD", "Chad", "تشاد / Tchad", "N'Djamena", "Africa", "Central Africa", 18278568L, 1284000.0, "Central African CFA Franc (XAF)", listOf("French", "Arabic"), "🇹🇩", 15.4542, 18.7322, 12.1348, 15.0557, 1.0, "North-central African country spanning the Sahara Desert in the north to fertile savannas in the south."),
            c("ET", "Ethiopia", "ኢትዮጵያ", "Addis Ababa", "Africa", "East Africa", 126527060L, 1104300.0, "Ethiopian Birr (ETB)", listOf("Amharic", "Oromo"), "🇪🇹", 9.1450, 40.4897, 9.0320, 38.7469, 3.0, "In the Horn of Africa, a rugged landlocked country split by the Great Rift Valley with ancient Lalibela churches."),
            c("SO", "Somalia", "Soomaaliya / الصومال", "Mogadishu", "Africa", "East Africa", 18143378L, 637657.0, "Somali Shilling (SOS)", listOf("Somali", "Arabic"), "🇸🇴", 5.1521, 46.1996, 2.0469, 45.3182, 3.0, "Located in the Horn of Africa with the longest coastline on the African mainland along the Indian Ocean."),
            c("KE", "Kenya", "Kenya", "Nairobi", "Africa", "East Africa", 55100586L, 580367.0, "Kenyan Shilling (KES)", listOf("Swahili", "English"), "🇰🇪", -0.0236, 37.9062, -1.2921, 36.8219, 3.0, "East African country with Indian Ocean coastline, Maasai Mara wildlife reserves, and Mount Kenya."),
            c("TZ", "Tanzania", "Tanzania", "Dodoma", "Africa", "East Africa", 67438106L, 947303.0, "Tanzanian Shilling (TZS)", listOf("Swahili", "English"), "🇹🇿", -6.3690, 34.8888, -6.1630, 35.7516, 3.0, "East African nation famed for Serengeti National Park, Mount Kilimanjaro, and Zanzibar archipelago."),
            c("UG", "Uganda", "Uganda", "Kampala", "Africa", "East Africa", 48582334L, 241550.0, "Ugandan Shilling (UGX)", listOf("English", "Swahili"), "🇺🇬", 1.3733, 32.2903, 0.3476, 32.5825, 3.0, "Landlocked East African country encompassing Lake Victoria, the snow-capped Rwenzori Mountains, and Nile source."),
            c("CD", "DR Congo", "RD Congo", "Kinshasa", "Africa", "Central Africa", 102262808L, 2344858.0, "Congolese Franc (CDF)", listOf("French", "Lingala", "Swahili"), "🇨🇩", -4.0383, 21.7587, -4.4419, 15.2663, 1.0, "Vast Central African country covered by the Congo Basin rainforest and crossed by the Congo River."),
            c("AO", "Angola", "Angola", "Luanda", "Africa", "Southern Africa", 36684202L, 1246700.0, "Angolan Kwanza (AOA)", listOf("Portuguese"), "🇦🇴", -11.2027, 17.8739, -8.8390, 13.2894, 1.0, "Southern African nation with Atlantic coastline, Kalandula Falls, and diverse sub-Saharan landscapes."),
            c("NA", "Namibia", "Namibia", "Windhoek", "Africa", "Southern Africa", 2604172L, 825615.0, "Namibian Dollar (NAD)", listOf("English"), "🇳🇦", -22.9576, 18.4904, -22.5609, 17.0658, 2.0, "Southwest African nation distinguished by the Namib Desert along the Atlantic Ocean and Etosha National Park."),
            c("BW", "Botswana", "Botswana", "Gaborone", "Africa", "Southern Africa", 2675352L, 581730.0, "Botswana Pula (BWP)", listOf("English", "Setswana"), "🇧🇼", -22.3285, 24.6849, -24.6282, 25.9231, 2.0, "Landlocked country in Southern Africa defined by the Kalahari Desert and the lush Okavango Delta."),
            c("ZW", "Zimbabwe", "Zimbabwe", "Harare", "Africa", "Southern Africa", 16665409L, 390757.0, "Zimbabwe Gold (ZiG)", listOf("English", "Shona", "Ndebele"), "🇿🇼", -19.0154, 29.1549, -17.8252, 31.0335, 2.0, "Landlocked country in southern Africa known for dramatic Victoria Falls on the Zambezi River."),
            c("ZM", "Zambia", "Zambia", "Lusaka", "Africa", "Southern Africa", 20569737L, 752618.0, "Zambian Kwacha (ZMW)", listOf("English"), "🇿🇲", -13.1339, 27.8493, -15.3875, 28.3228, 2.0, "Southern African nation of rugged terrain and diverse wildlife, sharing Victoria Falls with Zimbabwe."),
            c("MZ", "Mozambique", "Moçambique", "Maputo", "Africa", "Southeastern Africa", 33897354L, 801590.0, "Mozambican Metical (MZN)", listOf("Portuguese"), "🇲🇿", -18.6657, 35.5296, -25.9692, 32.5732, 2.0, "Southern African nation whose long Indian Ocean coastline is dotted with beaches and coral reefs."),
            c("MG", "Madagascar", "Madagasikara", "Antananarivo", "Africa", "East Africa", 30325732L, 587041.0, "Malagasy Ariary (MGA)", listOf("Malagasy", "French"), "🇲🇬", -18.7669, 46.8691, -18.8792, 47.5079, 3.0, "Fourth-largest island in the world off the southeast coast of Africa, home to lemurs and baobab avenues."),
            c("GH", "Ghana", "Ghana", "Accra", "Africa", "West Africa", 34121985L, 238533.0, "Ghanaian Cedi (GHS)", listOf("English"), "🇬🇭", 7.9465, -1.0232, 5.6037, -0.1870, 0.0, "West African nation on the Gulf of Guinea, known for Lake Volta, coastal forts, and rich Ashanti heritage."),
            c("CI", "Ivory Coast", "Côte d'Ivoire", "Yamoussoukro", "Africa", "West Africa", 28873034L, 322463.0, "West African CFA Franc (XOF)", listOf("French"), "🇨🇮", 7.5400, -5.5471, 6.8276, -5.2893, 0.0, "West African country with beach resorts, rainforests, and the economic hub of Abidjan."),
            c("SN", "Senegal", "Sénégal", "Dakar", "Africa", "West Africa", 17763163L, 196722.0, "West African CFA Franc (XOF)", listOf("French", "Wolof"), "🇸🇳", 14.4974, -14.4524, 14.7167, -17.4677, 0.0, "On the westernmost point of the African mainland, known for vibrant Dakar and Pink Lake Retba."),
            c("CM", "Cameroon", "Cameroun", "Yaoundé", "Africa", "Central Africa", 28647293L, 475442.0, "Central African CFA Franc (XAF)", listOf("French", "English"), "🇨🇲", 7.3697, 12.3547, 3.8480, 11.5021, 1.0, "Central African country of varied terrain and wildlife, often called 'Africa in miniature'."),
            c("SS", "South Sudan", "South Sudan", "Juba", "Africa", "East Africa", 11088796L, 619745.0, "South Sudanese Pound (SSP)", listOf("English"), "🇸🇸", 6.8770, 31.3070, 4.8594, 31.5713, 2.0, "Landlocked country in East-Central Africa traversed by the White Nile and the vast Sudd wetland."),

            // ================= AMERICAS =================
            c("MX", "Mexico", "México", "Mexico City", "North America", "Central/North America", 128455567L, 1964375.0, "Mexican Peso (MXN)", listOf("Spanish"), "🇲🇽", 23.6345, -102.5528, 19.4326, -99.1332, -6.0, "Between the U.S. and Central America, famous for Pacific and Gulf of Mexico beaches, Mayan ruins, and vibrant culture."),
            c("CU", "Cuba", "Cuba", "Havana", "North America", "Caribbean", 11194449L, 109884.0, "Cuban Peso (CUP)", listOf("Spanish"), "🇨🇺", 21.5218, -77.7812, 23.1136, -82.3666, -5.0, "Largest Caribbean island nation, known for sugar-white beaches, Old Havana architecture, and rich music."),
            c("GT", "Guatemala", "Guatemala", "Guatemala City", "North America", "Central America", 18092026L, 108889.0, "Guatemalan Quetzal (GTQ)", listOf("Spanish"), "🇬🇹", 15.7835, -90.2308, 14.6349, -90.5069, -6.0, "Central American country home to volcanoes, rainforests, and ancient Mayan sites like Tikal."),
            c("PA", "Panama", "Panamá", "Panama City", "North America", "Central America", 4468087L, 75417.0, "Balboa / USD", listOf("Spanish"), "🇵🇦", 8.5380, -80.7821, 8.9824, -79.5199, -5.0, "Isthmus linking Central and South America, world-famous for the Panama Canal connecting the Atlantic and Pacific."),
            c("CR", "Costa Rica", "Costa Rica", "San José", "North America", "Central America", 5212173L, 51100.0, "Costa Rican Colón (CRC)", listOf("Spanish"), "🇨🇷", 9.7489, -83.7534, 9.9281, -84.0907, -6.0, "Rugged, rainforested Central American country with coastlines on the Caribbean and Pacific, famed for biodiversity."),
            c("AR", "Argentina", "Argentina", "Buenos Aires", "South America", "South America", 46654581L, 2780400.0, "Argentine Peso (ARS)", listOf("Spanish"), "🇦🇷", -38.4161, -63.6167, -34.6037, -58.3816, -3.0, "Massive South American nation with terrain encompassing the Andes mountains, Pampas grasslands, Patagonia, and Iguazu Falls."),
            c("CL", "Chile", "Chile", "Santiago", "South America", "South America", 19629590L, 756102.0, "Chilean Peso (CLP)", listOf("Spanish"), "🇨🇱", -35.6751, -71.5430, -33.4489, -70.6693, -4.0, "Long, narrow country stretching along South America's western edge between the Andes and Pacific Ocean."),
            c("CO", "Colombia", "Colombia", "Bogotá", "South America", "South America", 52085168L, 1141748.0, "Colombian Peso (COP)", listOf("Spanish"), "🇨🇴", 4.5709, -74.2973, 4.7110, -74.0721, -5.0, "At the northern tip of South America with both Pacific and Caribbean coastlines, Amazon rainforest, and Andes peaks."),
            c("PE", "Peru", "Perú", "Lima", "South America", "South America", 34352719L, 1285216.0, "Peruvian Sol (PEN)", listOf("Spanish", "Quechua"), "🇵🇪", -9.1900, -75.0152, -12.0464, -77.0428, -5.0, "South American country home to a section of Amazon rainforest and Machu Picchu, an ancient Incan city high in the Andes."),
            c("VE", "Venezuela", "Venezuela", "Caracas", "South America", "South America", 28838499L, 916445.0, "Venezuelan Bolívar (VES)", listOf("Spanish"), "🇻🇪", 6.4238, -66.5897, 10.4806, -66.9036, -4.0, "On the northern coast of South America, home to Angel Falls, the world's highest uninterrupted waterfall."),
            c("EC", "Ecuador", "Ecuador", "Quito", "South America", "South America", 18190484L, 276841.0, "US Dollar (USD)", listOf("Spanish"), "🇪🇨", -1.8312, -78.1834, -0.1807, -78.4678, -5.0, "Straddling the equator on South America's west coast, encompassing Amazon jungle, Andean highlands, and Galápagos Islands."),
            c("BO", "Bolivia", "Bolivia", "La Paz / Sucre", "South America", "South America", 12388571L, 1098581.0, "Boliviano (BOB)", listOf("Spanish", "Quechua", "Aymara"), "🇧🇴", -16.2902, -63.5887, -16.4897, -68.1193, -4.0, "Central South American country with the Andes Mountains, Lake Titicaca, and Salar de Uyuni salt flats."),
            c("PY", "Paraguay", "Paraguay", "Asunción", "South America", "South America", 6861524L, 406752.0, "Paraguayan Guaraní (PYG)", listOf("Spanish", "Guaraní"), "🇵🇾", -23.4425, -58.4438, -25.2637, -57.5759, -4.0, "Landlocked country between Argentina, Brazil, and Bolivia, bisected by the Paraguay River."),
            c("UY", "Uruguay", "Uruguay", "Montevideo", "South America", "South America", 3423108L, 176215.0, "Uruguayan Peso (UYU)", listOf("Spanish"), "🇺🇾", -32.5228, -55.7658, -34.9011, -56.1645, -3.0, "South American country known for verdant interior and beach-lined coast along the Atlantic and Río de la Plata."),
            c("GY", "Guyana", "Guyana", "Georgetown", "South America", "South America", 813834L, 214969.0, "Guyanese Dollar (GYD)", listOf("English"), "🇬🇾", 4.8604, -58.9302, 6.8013, -58.1551, -4.0, "On South America's North Atlantic coast, defined by dense rainforest and Kaieteur Falls."),
            c("SR", "Suriname", "Suriname", "Paramaribo", "South America", "South America", 623236L, 163820.0, "Surinamese Dollar (SRD)", listOf("Dutch"), "🇸🇷", 3.9193, -56.0278, 5.8520, -55.2038, -3.0, "Small country on the northeastern coast of South America, covered by vast tropical rainforest."),
            c("GL", "Greenland", "Kalaallit Nunaat", "Nuuk", "North America", "Arctic", 56643L, 2166086.0, "Danish Krone (DKK)", listOf("Greenlandic", "Danish"), "🇬🇱", 71.7069, -42.6043, 64.1814, -51.6941, -3.0, "World's largest island, located between the Arctic and Atlantic oceans, famous for vast ice sheets and fjords."),

            // ================= OCEANIA =================
            c("NZ", "New Zealand", "Aotearoa", "Wellington", "Oceania", "Australasia", 5228100L, 268021.0, "New Zealand Dollar (NZD)", listOf("English", "Māori"), "🇳🇿", -40.9006, 174.8860, -41.2866, 174.7756, 12.0, "Island country in the southwestern Pacific Ocean consisting of two main landmasses, the North Island and South Island."),
            c("PG", "Papua New Guinea", "Papua Niugini", "Port Moresby", "Oceania", "Melanesia", 10329931L, 462840.0, "Papua New Guinean Kina (PGK)", listOf("English", "Tok Pisin"), "🇵🇬", -6.3150, 143.9555, -9.4438, 147.1803, 10.0, "In the southwestern Pacific, encompassing the eastern half of New Guinea and its offshore islands."),
            c("FJ", "Fiji", "Viti", "Suva", "Oceania", "Melanesia", 936375L, 18272.0, "Fijian Dollar (FJD)", listOf("English", "Fijian", "Hindi"), "🇫🇯", -17.7134, 178.0650, -18.1416, 178.4419, 12.0, "South Pacific archipelago of more than 300 islands famed for rugged landscapes, palm-lined beaches, and coral reefs.")
        )
    }
}
