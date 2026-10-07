package com.example.data.model

data class GeoPoint(
    val lat: Double,
    val lon: Double
)

data class CountryInfo(
    val id: String,
    val name: String,
    val nativeName: String,
    val capital: String,
    val continent: String,
    val region: String,
    val population: Long,
    val areaKm2: Double,
    val currency: String,
    val languages: List<String>,
    val flagEmoji: String,
    val centerLat: Double,
    val centerLon: Double,
    val capitalLat: Double = centerLat,
    val capitalLon: Double = centerLon,
    val timeZoneOffsetHours: Double,
    val description: String,
    val boundary: List<GeoPoint> = emptyList()
)

object WorldGeographicData {
    val countries: List<CountryInfo> by lazy {
        listOf(
            CountryInfo(
                id = "PK",
                name = "Pakistan",
                nativeName = "پاکستان",
                capital = "Islamabad",
                continent = "Asia",
                region = "South Asia",
                population = 241499431L,
                areaKm2 = 881913.0,
                currency = "Pakistani Rupee (PKR)",
                languages = listOf("Urdu", "English", "Punjabi", "Sindhi", "Pashto", "Balochi"),
                flagEmoji = "🇵🇰",
                centerLat = 30.3753,
                centerLon = 69.3451,
                capitalLat = 33.6844,
                capitalLon = 73.0479,
                timeZoneOffsetHours = 5.0,
                description = "Pakistan is located in South Asia with coastlines along the Arabian Sea and Gulf of Oman. Home to K2, the world's second-highest mountain peak, and the ancient Indus Valley Civilization.",
                boundary = listOf(
                    GeoPoint(23.7, 68.1),
                    GeoPoint(24.5, 67.3),
                    GeoPoint(25.1, 66.5),
                    GeoPoint(25.3, 64.6),
                    GeoPoint(25.2, 62.3),
                    GeoPoint(25.1, 61.5),
                    GeoPoint(27.0, 61.7),
                    GeoPoint(28.0, 62.0),
                    GeoPoint(29.5, 61.0),
                    GeoPoint(29.9, 61.8),
                    GeoPoint(29.5, 63.3),
                    GeoPoint(29.9, 64.4),
                    GeoPoint(31.0, 66.4),
                    GeoPoint(32.1, 69.2),
                    GeoPoint(33.5, 70.0),
                    GeoPoint(34.1, 71.1),
                    GeoPoint(35.5, 71.6),
                    GeoPoint(36.8, 71.8),
                    GeoPoint(37.1, 74.8),
                    GeoPoint(36.0, 75.5),
                    GeoPoint(35.5, 76.8),
                    GeoPoint(34.8, 76.5),
                    GeoPoint(34.5, 74.3),
                    GeoPoint(32.5, 74.5),
                    GeoPoint(31.6, 74.6),
                    GeoPoint(30.6, 73.9),
                    GeoPoint(29.9, 72.8),
                    GeoPoint(28.3, 70.3),
                    GeoPoint(27.2, 69.4),
                    GeoPoint(25.0, 71.1),
                    GeoPoint(24.2, 70.8),
                    GeoPoint(23.7, 68.1)
                )
            ),
            CountryInfo(
                id = "IN",
                name = "India",
                nativeName = "भारत",
                capital = "New Delhi",
                continent = "Asia",
                region = "South Asia",
                population = 1428627663L,
                areaKm2 = 3287263.0,
                currency = "Indian Rupee (INR)",
                languages = listOf("Hindi", "English"),
                flagEmoji = "🇮🇳",
                centerLat = 20.5937,
                centerLon = 78.9629,
                capitalLat = 28.6139,
                capitalLon = 77.2090,
                timeZoneOffsetHours = 5.5,
                description = "India is the most populous country in the world, renowned for its rich culture, history, and diverse landscapes from the Himalayas to tropical coasts.",
                boundary = listOf(
                    GeoPoint(34.5, 74.3),
                    GeoPoint(32.5, 74.5),
                    GeoPoint(31.6, 74.6),
                    GeoPoint(29.9, 72.8),
                    GeoPoint(28.3, 70.3),
                    GeoPoint(25.0, 71.1),
                    GeoPoint(23.7, 68.1),
                    GeoPoint(22.0, 69.0),
                    GeoPoint(20.5, 72.8),
                    GeoPoint(15.0, 73.8),
                    GeoPoint(10.0, 76.0),
                    GeoPoint(8.1, 77.5),
                    GeoPoint(9.5, 79.0),
                    GeoPoint(13.0, 80.3),
                    GeoPoint(17.7, 83.3),
                    GeoPoint(21.5, 87.0),
                    GeoPoint(22.5, 89.0),
                    GeoPoint(25.0, 89.5),
                    GeoPoint(26.5, 89.8),
                    GeoPoint(28.0, 88.5),
                    GeoPoint(28.5, 80.0),
                    GeoPoint(31.0, 78.5),
                    GeoPoint(34.5, 74.3)
                )
            ),
            CountryInfo(
                id = "CN",
                name = "China",
                nativeName = "中国",
                capital = "Beijing",
                continent = "Asia",
                region = "East Asia",
                population = 1411750000L,
                areaKm2 = 9596961.0,
                currency = "Chinese Yuan (CNY)",
                languages = listOf("Mandarin"),
                flagEmoji = "🇨🇳",
                centerLat = 35.8617,
                centerLon = 104.1954,
                capitalLat = 39.9042,
                capitalLon = 116.4074,
                timeZoneOffsetHours = 8.0,
                description = "China is one of the world's oldest civilizations with thousands of years of continuous history, renowned for the Great Wall and vibrant innovation.",
                boundary = listOf(
                    GeoPoint(37.1, 74.8),
                    GeoPoint(39.5, 75.0),
                    GeoPoint(45.0, 82.0),
                    GeoPoint(48.0, 87.0),
                    GeoPoint(49.0, 117.0),
                    GeoPoint(53.5, 124.0),
                    GeoPoint(48.0, 134.0),
                    GeoPoint(42.5, 130.5),
                    GeoPoint(40.0, 124.0),
                    GeoPoint(36.0, 120.0),
                    GeoPoint(31.2, 121.5),
                    GeoPoint(25.0, 119.0),
                    GeoPoint(22.5, 114.0),
                    GeoPoint(21.5, 108.5),
                    GeoPoint(22.0, 100.5),
                    GeoPoint(28.0, 97.5),
                    GeoPoint(28.0, 88.5),
                    GeoPoint(35.5, 76.8),
                    GeoPoint(37.1, 74.8)
                )
            ),
            CountryInfo(
                id = "IR",
                name = "Iran",
                nativeName = "ایران",
                capital = "Tehran",
                continent = "Asia",
                region = "Middle East",
                population = 88550570L,
                areaKm2 = 1648195.0,
                currency = "Iranian Rial (IRR)",
                languages = listOf("Persian"),
                flagEmoji = "🇮🇷",
                centerLat = 32.4279,
                centerLon = 53.6880,
                capitalLat = 35.6892,
                capitalLon = 51.3890,
                timeZoneOffsetHours = 3.5,
                description = "Iran has a rich heritage rooted in the ancient Persian Empire, famous for intricate architecture, literature, Persian carpets, and rich biodiversity.",
                boundary = listOf(
                    GeoPoint(25.1, 61.5),
                    GeoPoint(27.0, 61.7),
                    GeoPoint(29.9, 61.8),
                    GeoPoint(35.0, 61.0),
                    GeoPoint(37.5, 58.5),
                    GeoPoint(37.5, 54.0),
                    GeoPoint(38.5, 48.5),
                    GeoPoint(39.5, 44.5),
                    GeoPoint(36.5, 44.5),
                    GeoPoint(33.0, 46.0),
                    GeoPoint(30.0, 48.5),
                    GeoPoint(29.0, 50.5),
                    GeoPoint(27.0, 56.5),
                    GeoPoint(25.5, 59.0),
                    GeoPoint(25.1, 61.5)
                )
            ),
            CountryInfo(
                id = "RU",
                name = "Russia",
                nativeName = "Россия",
                capital = "Moscow",
                continent = "Europe",
                region = "Eastern Europe / Northern Asia",
                population = 144236933L,
                areaKm2 = 17098246.0,
                currency = "Russian Ruble (RUB)",
                languages = listOf("Russian"),
                flagEmoji = "🇷🇺",
                centerLat = 61.5240,
                centerLon = 105.3188,
                capitalLat = 55.7558,
                capitalLon = 37.6173,
                timeZoneOffsetHours = 3.0,
                description = "The largest country on Earth by land area, spanning 11 time zones across Eastern Europe and Northern Asia, known for its vast taiga and iconic history.",
                boundary = listOf(
                    GeoPoint(69.0, 31.0),
                    GeoPoint(68.0, 45.0),
                    GeoPoint(73.0, 70.0),
                    GeoPoint(76.0, 110.0),
                    GeoPoint(70.0, 160.0),
                    GeoPoint(65.0, 180.0),
                    GeoPoint(55.0, 165.0),
                    GeoPoint(45.0, 135.0),
                    GeoPoint(50.0, 120.0),
                    GeoPoint(50.0, 85.0),
                    GeoPoint(54.0, 60.0),
                    GeoPoint(55.0, 37.0),
                    GeoPoint(60.0, 30.0),
                    GeoPoint(69.0, 31.0)
                )
            ),
            CountryInfo(
                id = "AF",
                name = "Afghanistan",
                nativeName = "افغانستان",
                capital = "Kabul",
                continent = "Asia",
                region = "South Asia",
                population = 41128771L,
                areaKm2 = 652864.0,
                currency = "Afghan Afghani (AFN)",
                languages = listOf("Pashto", "Dari"),
                flagEmoji = "🇦🇫",
                centerLat = 33.9391,
                centerLon = 67.7100,
                capitalLat = 34.5553,
                capitalLon = 69.2075,
                timeZoneOffsetHours = 4.5,
                description = "Landlocked nation situated at the crossroads of Central and South Asia, featuring the rugged Hindu Kush mountain range.",
                boundary = listOf(
                    GeoPoint(29.5, 61.0),
                    GeoPoint(31.0, 66.4),
                    GeoPoint(33.5, 70.0),
                    GeoPoint(36.8, 71.8),
                    GeoPoint(37.5, 74.0),
                    GeoPoint(37.5, 70.5),
                    GeoPoint(37.0, 65.0),
                    GeoPoint(35.5, 61.5),
                    GeoPoint(29.5, 61.0)
                )
            ),
            CountryInfo(
                id = "SA",
                name = "Saudi Arabia",
                nativeName = "المملكة العربية السعودية",
                capital = "Riyadh",
                continent = "Asia",
                region = "Middle East",
                population = 36408820L,
                areaKm2 = 2149690.0,
                currency = "Saudi Riyal (SAR)",
                languages = listOf("Arabic"),
                flagEmoji = "🇸🇦",
                centerLat = 23.8859,
                centerLon = 45.0792,
                capitalLat = 24.7136,
                capitalLon = 46.6753,
                timeZoneOffsetHours = 3.0,
                description = "The largest country in the Arabian Peninsula, home to Islam's two holiest cities, Mecca and Medina, and magnificent desert landscapes.",
                boundary = listOf(
                    GeoPoint(16.5, 42.5),
                    GeoPoint(20.0, 40.0),
                    GeoPoint(28.0, 35.0),
                    GeoPoint(31.5, 37.0),
                    GeoPoint(30.0, 44.0),
                    GeoPoint(28.5, 48.0),
                    GeoPoint(25.0, 50.0),
                    GeoPoint(24.0, 55.0),
                    GeoPoint(19.0, 55.0),
                    GeoPoint(16.0, 52.0),
                    GeoPoint(16.5, 42.5)
                )
            ),
            CountryInfo(
                id = "EG",
                name = "Egypt",
                nativeName = "مصر",
                capital = "Cairo",
                continent = "Africa",
                region = "North Africa",
                population = 110990103L,
                areaKm2 = 1002450.0,
                currency = "Egyptian Pound (EGP)",
                languages = listOf("Arabic"),
                flagEmoji = "🇪🇬",
                centerLat = 26.8206,
                centerLon = 30.8025,
                capitalLat = 30.0444,
                capitalLon = 31.2357,
                timeZoneOffsetHours = 2.0,
                description = "Famed for its millennium-old pyramids, the Nile River, and Great Sphinx, connecting northeast Africa with the Middle East.",
                boundary = listOf(
                    GeoPoint(22.0, 25.0),
                    GeoPoint(31.5, 25.0),
                    GeoPoint(31.5, 31.0),
                    GeoPoint(31.2, 34.0),
                    GeoPoint(27.8, 34.3),
                    GeoPoint(22.0, 36.8),
                    GeoPoint(22.0, 25.0)
                )
            ),
            CountryInfo(
                id = "SD",
                name = "Sudan",
                nativeName = "السودان",
                capital = "Khartoum",
                continent = "Africa",
                region = "North Africa",
                population = 46874204L,
                areaKm2 = 1861484.0,
                currency = "Sudanese Pound (SDG)",
                languages = listOf("Arabic", "English"),
                flagEmoji = "🇸🇩",
                centerLat = 12.8628,
                centerLon = 30.2176,
                capitalLat = 15.5007,
                capitalLon = 32.5599,
                timeZoneOffsetHours = 2.0,
                description = "Located in Northeast Africa, known for ancient Nubian pyramids, the confluence of the Blue and White Niles, and rich savannahs.",
                boundary = listOf(
                    GeoPoint(22.0, 25.0),
                    GeoPoint(22.0, 36.8),
                    GeoPoint(18.0, 38.5),
                    GeoPoint(15.0, 36.5),
                    GeoPoint(12.0, 34.0),
                    GeoPoint(9.5, 33.5),
                    GeoPoint(10.0, 24.0),
                    GeoPoint(15.0, 23.0),
                    GeoPoint(20.0, 24.0),
                    GeoPoint(22.0, 25.0)
                )
            ),
            CountryInfo(
                id = "LY",
                name = "Libya",
                nativeName = "ليبيا",
                capital = "Tripoli",
                continent = "Africa",
                region = "North Africa",
                population = 6812341L,
                areaKm2 = 1759540.0,
                currency = "Libyan Dinar (LYD)",
                languages = listOf("Arabic"),
                flagEmoji = "🇱🇾",
                centerLat = 26.3351,
                centerLon = 17.2283,
                capitalLat = 32.8872,
                capitalLon = 13.1913,
                timeZoneOffsetHours = 2.0,
                description = "Situated in the Maghreb region of North Africa, bordering the Mediterranean Sea and possessing massive Sahara desert expanses.",
                boundary = listOf(
                    GeoPoint(20.0, 24.0),
                    GeoPoint(22.0, 25.0),
                    GeoPoint(31.5, 25.0),
                    GeoPoint(32.5, 20.0),
                    GeoPoint(31.0, 15.0),
                    GeoPoint(33.0, 11.5),
                    GeoPoint(30.0, 10.0),
                    GeoPoint(23.5, 12.0),
                    GeoPoint(19.5, 16.0),
                    GeoPoint(20.0, 24.0)
                )
            ),
            CountryInfo(
                id = "DZ",
                name = "Algeria",
                nativeName = "الجزائر",
                capital = "Algiers",
                continent = "Africa",
                region = "North Africa",
                population = 44903225L,
                areaKm2 = 2381741.0,
                currency = "Algerian Dinar (DZD)",
                languages = listOf("Arabic", "Tamazight"),
                flagEmoji = "🇩🇿",
                centerLat = 28.0339,
                centerLon = 1.6596,
                capitalLat = 36.7538,
                capitalLon = 3.0588,
                timeZoneOffsetHours = 1.0,
                description = "The largest country in Africa and tenth-largest in the world, spanning Mediterranean coastline and vast Sahara sand dunes.",
                boundary = listOf(
                    GeoPoint(19.0, 3.5),
                    GeoPoint(23.5, 12.0),
                    GeoPoint(30.0, 10.0),
                    GeoPoint(37.0, 8.5),
                    GeoPoint(36.0, 1.0),
                    GeoPoint(35.0, -2.0),
                    GeoPoint(31.0, -2.5),
                    GeoPoint(27.0, -8.5),
                    GeoPoint(21.0, -5.0),
                    GeoPoint(19.0, 3.5)
                )
            ),
            CountryInfo(
                id = "NG",
                name = "Nigeria",
                nativeName = "Nigeria",
                capital = "Abuja",
                continent = "Africa",
                region = "West Africa",
                population = 218541212L,
                areaKm2 = 923768.0,
                currency = "Nigerian Naira (NGN)",
                languages = listOf("English", "Hausa", "Yoruba", "Igbo"),
                flagEmoji = "🇳🇬",
                centerLat = 9.0820,
                centerLon = 8.6753,
                capitalLat = 9.0765,
                capitalLon = 7.3986,
                timeZoneOffsetHours = 1.0,
                description = "The most populous country in Africa, celebrated for its dynamic economy, Afrobeat music, Nollywood film industry, and cultural diversity.",
                boundary = listOf(
                    GeoPoint(4.5, 6.0),
                    GeoPoint(4.5, 8.5),
                    GeoPoint(7.0, 11.5),
                    GeoPoint(13.0, 14.0),
                    GeoPoint(13.8, 11.0),
                    GeoPoint(13.5, 5.0),
                    GeoPoint(10.0, 3.5),
                    GeoPoint(6.5, 3.0),
                    GeoPoint(4.5, 6.0)
                )
            ),
            CountryInfo(
                id = "NO",
                name = "Norway",
                nativeName = "Norge",
                capital = "Oslo",
                continent = "Europe",
                region = "Northern Europe",
                population = 5457127L,
                areaKm2 = 385207.0,
                currency = "Norwegian Krone (NOK)",
                languages = listOf("Norwegian"),
                flagEmoji = "🇳🇴",
                centerLat = 60.4720,
                centerLon = 8.4689,
                capitalLat = 59.9139,
                capitalLon = 10.7522,
                timeZoneOffsetHours = 1.0,
                description = "Renowned for stunning fjords, northern lights, viking history, dramatic mountains, and world-leading human development index.",
                boundary = listOf(
                    GeoPoint(58.0, 7.0),
                    GeoPoint(62.0, 5.0),
                    GeoPoint(65.0, 11.0),
                    GeoPoint(70.0, 20.0),
                    GeoPoint(71.0, 28.0),
                    GeoPoint(69.0, 31.0),
                    GeoPoint(69.0, 25.0),
                    GeoPoint(64.0, 14.0),
                    GeoPoint(59.0, 11.0),
                    GeoPoint(58.0, 7.0)
                )
            ),
            CountryInfo(
                id = "DE",
                name = "Germany",
                nativeName = "Deutschland",
                capital = "Berlin",
                continent = "Europe",
                region = "Western Europe",
                population = 84432670L,
                areaKm2 = 357022.0,
                currency = "Euro (EUR)",
                languages = listOf("German"),
                flagEmoji = "🇩🇪",
                centerLat = 51.1657,
                centerLon = 10.4515,
                capitalLat = 52.5200,
                capitalLon = 13.4050,
                timeZoneOffsetHours = 1.0,
                description = "Europe's largest economy, known for rich cultural history, precision engineering, castles, forests, and modern arts.",
                boundary = listOf(
                    GeoPoint(47.5, 8.0),
                    GeoPoint(49.0, 6.0),
                    GeoPoint(51.0, 6.0),
                    GeoPoint(53.5, 7.0),
                    GeoPoint(55.0, 9.0),
                    GeoPoint(54.0, 14.0),
                    GeoPoint(51.0, 15.0),
                    GeoPoint(49.0, 13.5),
                    GeoPoint(47.5, 12.0),
                    GeoPoint(47.5, 8.0)
                )
            ),
            CountryInfo(
                id = "FR",
                name = "France",
                nativeName = "France",
                capital = "Paris",
                continent = "Europe",
                region = "Western Europe",
                population = 67971000L,
                areaKm2 = 643801.0,
                currency = "Euro (EUR)",
                languages = listOf("French"),
                flagEmoji = "🇫🇷",
                centerLat = 46.2276,
                centerLon = 2.2137,
                capitalLat = 48.8566,
                capitalLon = 2.3522,
                timeZoneOffsetHours = 1.0,
                description = "Famous for Paris, the Eiffel Tower, fine wine, cuisine, history, art museums, and picturesque Alpine and Mediterranean scenery.",
                boundary = listOf(
                    GeoPoint(42.5, 3.0),
                    GeoPoint(43.5, -1.5),
                    GeoPoint(47.0, -2.0),
                    GeoPoint(49.0, -1.0),
                    GeoPoint(51.0, 2.5),
                    GeoPoint(49.0, 6.0),
                    GeoPoint(46.0, 7.0),
                    GeoPoint(43.5, 7.0),
                    GeoPoint(42.5, 3.0)
                )
            ),
            CountryInfo(
                id = "GB",
                name = "United Kingdom",
                nativeName = "United Kingdom",
                capital = "London",
                continent = "Europe",
                region = "Northern Europe",
                population = 67736802L,
                areaKm2 = 242495.0,
                currency = "British Pound (GBP)",
                languages = listOf("English"),
                flagEmoji = "🇬🇧",
                centerLat = 55.3781,
                centerLon = -3.4360,
                capitalLat = 51.5074,
                capitalLon = -0.1278,
                timeZoneOffsetHours = 0.0,
                description = "Island nation in northwestern Europe, composed of England, Scotland, Wales, and Northern Ireland, with centuries of global influence.",
                boundary = listOf(
                    GeoPoint(50.0, -5.0),
                    GeoPoint(51.0, 1.5),
                    GeoPoint(53.0, 0.5),
                    GeoPoint(55.0, -1.5),
                    GeoPoint(58.5, -3.0),
                    GeoPoint(58.0, -5.0),
                    GeoPoint(55.0, -5.0),
                    GeoPoint(52.0, -5.0),
                    GeoPoint(50.0, -5.0)
                )
            ),
            CountryInfo(
                id = "MN",
                name = "Mongolia",
                nativeName = "Монгол улс",
                capital = "Ulaanbaatar",
                continent = "Asia",
                region = "East Asia",
                population = 3398366L,
                areaKm2 = 1564116.0,
                currency = "Mongolian Tögrög (MNT)",
                languages = listOf("Mongolian"),
                flagEmoji = "🇲🇳",
                centerLat = 46.8625,
                centerLon = 103.8467,
                capitalLat = 47.8864,
                capitalLon = 106.9057,
                timeZoneOffsetHours = 8.0,
                description = "Known for rugged nomadic culture, the vast Gobi Desert, Genghis Khan's heritage, and expansive wild grasslands.",
                boundary = listOf(
                    GeoPoint(42.0, 90.0),
                    GeoPoint(48.0, 88.0),
                    GeoPoint(52.0, 98.0),
                    GeoPoint(50.0, 115.0),
                    GeoPoint(47.0, 120.0),
                    GeoPoint(42.0, 105.0),
                    GeoPoint(42.0, 90.0)
                )
            ),
            CountryInfo(
                id = "JP",
                name = "Japan",
                nativeName = "日本",
                capital = "Tokyo",
                continent = "Asia",
                region = "East Asia",
                population = 125124986L,
                areaKm2 = 377975.0,
                currency = "Japanese Yen (JPY)",
                languages = listOf("Japanese"),
                flagEmoji = "🇯🇵",
                centerLat = 36.2048,
                centerLon = 138.2529,
                capitalLat = 35.6762,
                capitalLon = 139.6503,
                timeZoneOffsetHours = 9.0,
                description = "Archipelago nation celebrated for cutting-edge technology, Mount Fuji, cherry blossoms, historic temples, and manga/anime culture.",
                boundary = listOf(
                    GeoPoint(31.0, 130.5),
                    GeoPoint(34.0, 131.0),
                    GeoPoint(36.0, 136.0),
                    GeoPoint(41.0, 140.0),
                    GeoPoint(45.5, 142.0),
                    GeoPoint(43.0, 145.0),
                    GeoPoint(36.0, 141.0),
                    GeoPoint(33.0, 136.0),
                    GeoPoint(31.0, 130.5)
                )
            ),
            CountryInfo(
                id = "ID",
                name = "Indonesia",
                nativeName = "Indonesia",
                capital = "Jakarta",
                continent = "Asia",
                region = "Southeast Asia",
                population = 277534122L,
                areaKm2 = 1904569.0,
                currency = "Indonesian Rupiah (IDR)",
                languages = listOf("Indonesian"),
                flagEmoji = "🇮🇩",
                centerLat = -0.7893,
                centerLon = 113.9213,
                capitalLat = -6.2088,
                capitalLon = 106.8456,
                timeZoneOffsetHours = 7.0,
                description = "The world's largest island country with over 17,000 islands, featuring tropical rainforests, volcanic landscapes, and diverse fauna.",
                boundary = listOf(
                    GeoPoint(5.5, 95.0),
                    GeoPoint(0.0, 104.0),
                    GeoPoint(-6.0, 106.0),
                    GeoPoint(-8.5, 115.0),
                    GeoPoint(-9.0, 125.0),
                    GeoPoint(-4.0, 140.0),
                    GeoPoint(2.0, 128.0),
                    GeoPoint(4.0, 118.0),
                    GeoPoint(1.0, 109.0),
                    GeoPoint(5.5, 95.0)
                )
            ),
            CountryInfo(
                id = "AU",
                name = "Australia",
                nativeName = "Australia",
                capital = "Canberra",
                continent = "Oceania",
                region = "Australasia",
                population = 26005540L,
                areaKm2 = 7692024.0,
                currency = "Australian Dollar (AUD)",
                languages = listOf("English"),
                flagEmoji = "🇦🇺",
                centerLat = -25.2744,
                centerLon = 133.7751,
                capitalLat = -35.2809,
                capitalLon = 149.1300,
                timeZoneOffsetHours = 10.0,
                description = "Island continent famous for the Sydney Opera House, Great Barrier Reef, the vast Outback, and unique wildlife like kangaroos and koalas.",
                boundary = listOf(
                    GeoPoint(-12.0, 130.0),
                    GeoPoint(-11.0, 142.0),
                    GeoPoint(-16.0, 146.0),
                    GeoPoint(-28.0, 153.5),
                    GeoPoint(-37.5, 150.0),
                    GeoPoint(-38.0, 141.0),
                    GeoPoint(-32.0, 125.0),
                    GeoPoint(-34.0, 115.0),
                    GeoPoint(-22.0, 114.0),
                    GeoPoint(-14.0, 126.0),
                    GeoPoint(-12.0, 130.0)
                )
            ),
            CountryInfo(
                id = "US",
                name = "United States",
                nativeName = "United States",
                capital = "Washington, D.C.",
                continent = "North America",
                region = "Northern America",
                population = 333287557L,
                areaKm2 = 9833517.0,
                currency = "US Dollar (USD)",
                languages = listOf("English"),
                flagEmoji = "🇺🇸",
                centerLat = 37.0902,
                centerLon = -95.7129,
                capitalLat = 38.9072,
                capitalLon = -77.0369,
                timeZoneOffsetHours = -5.0,
                description = "Spanning 50 states across North America, famous for Hollywood, national parks, technological innovation, and vibrant cultural influence.",
                boundary = listOf(
                    GeoPoint(49.0, -123.0),
                    GeoPoint(49.0, -95.0),
                    GeoPoint(45.0, -75.0),
                    GeoPoint(44.0, -69.0),
                    GeoPoint(30.0, -81.0),
                    GeoPoint(25.0, -80.0),
                    GeoPoint(29.0, -89.0),
                    GeoPoint(26.0, -97.0),
                    GeoPoint(31.5, -106.0),
                    GeoPoint(32.5, -117.0),
                    GeoPoint(38.0, -123.0),
                    GeoPoint(49.0, -123.0)
                )
            ),
            CountryInfo(
                id = "BR",
                name = "Brazil",
                nativeName = "Brasil",
                capital = "Brasília",
                continent = "South America",
                region = "South America",
                population = 214326223L,
                areaKm2 = 8515767.0,
                currency = "Brazilian Real (BRL)",
                languages = listOf("Portuguese"),
                flagEmoji = "🇧🇷",
                centerLat = -14.2350,
                centerLon = -51.9253,
                capitalLat = -15.7975,
                capitalLon = -47.8919,
                timeZoneOffsetHours = -3.0,
                description = "Largest country in South America, containing the Amazon Rainforest, Rio de Janeiro Carnival, and legendary football tradition.",
                boundary = listOf(
                    GeoPoint(4.0, -51.0),
                    GeoPoint(-5.0, -35.0),
                    GeoPoint(-13.0, -38.5),
                    GeoPoint(-23.0, -43.0),
                    GeoPoint(-30.0, -50.0),
                    GeoPoint(-33.0, -53.0),
                    GeoPoint(-25.0, -54.0),
                    GeoPoint(-15.0, -60.0),
                    GeoPoint(-7.0, -73.0),
                    GeoPoint(2.0, -67.0),
                    GeoPoint(4.0, -51.0)
                )
            ),
            CountryInfo(
                id = "TR",
                name = "Turkey",
                nativeName = "Türkiye",
                capital = "Ankara",
                continent = "Asia",
                region = "Middle East / Europe",
                population = 85341241L,
                areaKm2 = 783562.0,
                currency = "Turkish Lira (TRY)",
                languages = listOf("Turkish"),
                flagEmoji = "🇹🇷",
                centerLat = 38.9637,
                centerLon = 35.2433,
                capitalLat = 39.9334,
                capitalLon = 32.8597,
                timeZoneOffsetHours = 3.0,
                description = "Straddling Europe and Asia across the Bosphorus Strait, rich with Byzantine and Ottoman monuments like the Hagia Sophia.",
                boundary = listOf(
                    GeoPoint(41.0, 26.5),
                    GeoPoint(41.5, 32.0),
                    GeoPoint(41.0, 41.5),
                    GeoPoint(39.5, 44.5),
                    GeoPoint(37.0, 43.0),
                    GeoPoint(36.5, 36.0),
                    GeoPoint(36.0, 31.0),
                    GeoPoint(38.0, 26.5),
                    GeoPoint(41.0, 26.5)
                )
            ),
            CountryInfo(
                id = "AE",
                name = "United Arab Emirates",
                nativeName = "الإمارات العربية المتحدة",
                capital = "Abu Dhabi",
                continent = "Asia",
                region = "Middle East",
                population = 9441129L,
                areaKm2 = 83600.0,
                currency = "UAE Dirham (AED)",
                languages = listOf("Arabic"),
                flagEmoji = "🇦🇪",
                centerLat = 23.4241,
                centerLon = 53.8478,
                capitalLat = 24.4539,
                capitalLon = 54.3773,
                timeZoneOffsetHours = 4.0,
                description = "Modern federation on the Arabian Gulf, home to Dubai's Burj Khalifa, luxurious architecture, desert safaris, and trade hubs.",
                boundary = listOf(
                    GeoPoint(24.0, 51.5),
                    GeoPoint(25.5, 54.5),
                    GeoPoint(26.0, 56.0),
                    GeoPoint(25.0, 56.5),
                    GeoPoint(23.0, 55.5),
                    GeoPoint(24.0, 51.5)
                )
            ),
            CountryInfo(
                id = "ZA",
                name = "South Africa",
                nativeName = "South Africa",
                capital = "Pretoria",
                continent = "Africa",
                region = "Southern Africa",
                population = 59893885L,
                areaKm2 = 1221037.0,
                currency = "South African Rand (ZAR)",
                languages = listOf("Zulu", "Xhosa", "Afrikaans", "English"),
                flagEmoji = "🇿🇦",
                centerLat = -30.5595,
                centerLon = 22.9375,
                capitalLat = -25.7479,
                capitalLon = 28.2293,
                timeZoneOffsetHours = 2.0,
                description = "The Rainbow Nation at the southernmost tip of Africa, famed for Table Mountain, Kruger National Park, and Nelson Mandela's legacy.",
                boundary = listOf(
                    GeoPoint(-22.0, 30.0),
                    GeoPoint(-26.0, 32.5),
                    GeoPoint(-31.0, 30.0),
                    GeoPoint(-34.0, 26.0),
                    GeoPoint(-34.8, 20.0),
                    GeoPoint(-33.0, 18.0),
                    GeoPoint(-28.5, 16.5),
                    GeoPoint(-25.0, 20.0),
                    GeoPoint(-22.0, 30.0)
                )
            ),
            CountryInfo(
                id = "CA",
                name = "Canada",
                nativeName = "Canada",
                capital = "Ottawa",
                continent = "North America",
                region = "Northern America",
                population = 40097761L,
                areaKm2 = 9984670.0,
                currency = "Canadian Dollar (CAD)",
                languages = listOf("English", "French"),
                flagEmoji = "🇨🇦",
                centerLat = 56.1304,
                centerLon = -106.3468,
                capitalLat = 45.4215,
                capitalLon = -75.6972,
                timeZoneOffsetHours = -5.0,
                description = "The second-largest country in the world by area, renowned for Niagara Falls, Banff National Park, maple leaf heritage, and vast wilderness.",
                boundary = listOf(
                    GeoPoint(49.0, -123.0),
                    GeoPoint(60.0, -140.0),
                    GeoPoint(70.0, -130.0),
                    GeoPoint(68.0, -90.0),
                    GeoPoint(60.0, -65.0),
                    GeoPoint(45.0, -66.0),
                    GeoPoint(45.0, -75.0),
                    GeoPoint(49.0, -95.0),
                    GeoPoint(49.0, -123.0)
                )
            ),
            CountryInfo(
                id = "IT",
                name = "Italy",
                nativeName = "Italia",
                capital = "Rome",
                continent = "Europe",
                region = "Southern Europe",
                population = 58870762L,
                areaKm2 = 301340.0,
                currency = "Euro (EUR)",
                languages = listOf("Italian"),
                flagEmoji = "🇮🇹",
                centerLat = 41.8719,
                centerLon = 12.5674,
                capitalLat = 41.9028,
                capitalLon = 12.4964,
                timeZoneOffsetHours = 1.0,
                description = "Mediterranean peninsula famed for ancient Roman landmarks like the Colosseum, Renaissance art, Venice canals, and world-famous cuisine.",
                boundary = listOf(
                    GeoPoint(46.0, 7.0),
                    GeoPoint(46.5, 13.5),
                    GeoPoint(40.0, 18.5),
                    GeoPoint(38.0, 15.5),
                    GeoPoint(41.5, 12.5),
                    GeoPoint(44.0, 8.0),
                    GeoPoint(46.0, 7.0)
                )
            ),
            CountryInfo(
                id = "ES",
                name = "Spain",
                nativeName = "España",
                capital = "Madrid",
                continent = "Europe",
                region = "Southern Europe",
                population = 48373336L,
                areaKm2 = 505992.0,
                currency = "Euro (EUR)",
                languages = listOf("Spanish"),
                flagEmoji = "🇪🇸",
                centerLat = 40.4637,
                centerLon = -3.7492,
                capitalLat = 40.4168,
                capitalLon = -3.7038,
                timeZoneOffsetHours = 1.0,
                description = "Located on the Iberian Peninsula, celebrated for historic architecture, flamenco music, vibrant cities like Madrid and Barcelona, and sunny coasts.",
                boundary = listOf(
                    GeoPoint(43.5, -8.5),
                    GeoPoint(43.5, -1.5),
                    GeoPoint(42.5, 3.0),
                    GeoPoint(36.0, -5.5),
                    GeoPoint(37.0, -7.5),
                    GeoPoint(42.0, -8.5),
                    GeoPoint(43.5, -8.5)
                )
            ),
            CountryInfo(
                id = "MY",
                name = "Malaysia",
                nativeName = "Malaysia",
                capital = "Kuala Lumpur",
                continent = "Asia",
                region = "Southeast Asia",
                population = 34308525L,
                areaKm2 = 330803.0,
                currency = "Malaysian Ringgit (MYR)",
                languages = listOf("Malay", "English"),
                flagEmoji = "🇲🇾",
                centerLat = 4.2105,
                centerLon = 101.9758,
                capitalLat = 3.1390,
                capitalLon = 101.6869,
                timeZoneOffsetHours = 8.0,
                description = "Southeast Asian nation known for the iconic Petronas Twin Towers in Kuala Lumpur, lush tropical rainforests, and vibrant multicultural heritage.",
                boundary = listOf(
                    GeoPoint(6.5, 100.2),
                    GeoPoint(6.2, 102.2),
                    GeoPoint(1.3, 104.2),
                    GeoPoint(1.3, 103.5),
                    GeoPoint(3.0, 101.2),
                    GeoPoint(6.5, 100.2)
                )
            )
        )
    }

    // World Landmass Polygons for continents rendering
    val continents: List<List<GeoPoint>> by lazy {
        listOf(
            // Eurasia main body
            listOf(
                GeoPoint(36.0, -5.5), GeoPoint(43.5, -9.0), GeoPoint(48.0, -4.5),
                GeoPoint(53.5, 5.0), GeoPoint(58.0, 11.0), GeoPoint(68.0, 14.0),
                GeoPoint(71.0, 28.0), GeoPoint(69.0, 60.0), GeoPoint(73.0, 80.0),
                GeoPoint(76.0, 110.0), GeoPoint(72.0, 140.0), GeoPoint(66.0, 170.0),
                GeoPoint(60.0, 165.0), GeoPoint(53.0, 141.0), GeoPoint(43.0, 132.0),
                GeoPoint(35.0, 129.0), GeoPoint(30.0, 122.0), GeoPoint(22.0, 114.0),
                GeoPoint(10.0, 104.0), GeoPoint(1.3, 103.8), GeoPoint(8.0, 98.0),
                GeoPoint(21.0, 89.0), GeoPoint(13.0, 80.0), GeoPoint(8.1, 77.5),
                GeoPoint(22.0, 69.0), GeoPoint(25.0, 62.0), GeoPoint(25.0, 56.5),
                GeoPoint(12.8, 45.0), GeoPoint(20.0, 40.0), GeoPoint(31.0, 34.0),
                GeoPoint(36.0, 36.0), GeoPoint(41.0, 28.0), GeoPoint(38.0, 23.0),
                GeoPoint(40.0, 18.0), GeoPoint(44.0, 8.0), GeoPoint(36.0, -5.5)
            ),
            // Africa main body
            listOf(
                GeoPoint(36.0, -5.5), GeoPoint(37.0, 10.0), GeoPoint(32.0, 14.0),
                GeoPoint(31.5, 32.0), GeoPoint(22.0, 37.0), GeoPoint(12.0, 43.5),
                GeoPoint(11.8, 51.2), GeoPoint(0.0, 42.5), GeoPoint(-15.0, 40.5),
                GeoPoint(-26.0, 33.0), GeoPoint(-34.5, 20.0), GeoPoint(-33.0, 18.0),
                GeoPoint(-18.0, 12.0), GeoPoint(-5.0, 11.5), GeoPoint(4.5, 8.5),
                GeoPoint(4.5, 1.0), GeoPoint(5.0, -3.0), GeoPoint(4.5, -8.0),
                GeoPoint(14.5, -17.5), GeoPoint(21.0, -17.0), GeoPoint(28.0, -12.0),
                GeoPoint(35.5, -6.0), GeoPoint(36.0, -5.5)
            ),
            // North America main body
            listOf(
                GeoPoint(70.0, -160.0), GeoPoint(72.0, -130.0), GeoPoint(68.0, -90.0),
                GeoPoint(60.0, -65.0), GeoPoint(50.0, -55.0), GeoPoint(44.0, -66.0),
                GeoPoint(30.0, -81.0), GeoPoint(25.0, -80.0), GeoPoint(21.0, -87.0),
                GeoPoint(15.0, -85.0), GeoPoint(8.0, -78.0), GeoPoint(8.5, -83.0),
                GeoPoint(16.0, -93.0), GeoPoint(20.0, -105.0), GeoPoint(30.0, -115.0),
                GeoPoint(38.0, -123.0), GeoPoint(50.0, -128.0), GeoPoint(60.0, -145.0),
                GeoPoint(65.0, -168.0), GeoPoint(70.0, -160.0)
            ),
            // South America main body
            listOf(
                GeoPoint(12.0, -72.0), GeoPoint(10.5, -62.0), GeoPoint(5.0, -52.0),
                GeoPoint(-5.0, -35.0), GeoPoint(-15.0, -39.0), GeoPoint(-23.0, -43.0),
                GeoPoint(-34.0, -53.0), GeoPoint(-42.0, -64.0), GeoPoint(-55.0, -67.0),
                GeoPoint(-52.0, -75.0), GeoPoint(-37.0, -73.0), GeoPoint(-18.0, -71.0),
                GeoPoint(-5.0, -81.0), GeoPoint(2.0, -78.0), GeoPoint(8.0, -77.0),
                GeoPoint(12.0, -72.0)
            ),
            // Australia main body
            listOf(
                GeoPoint(-12.0, 130.0), GeoPoint(-11.0, 142.0), GeoPoint(-16.0, 146.0),
                GeoPoint(-28.0, 153.5), GeoPoint(-37.5, 150.0), GeoPoint(-38.0, 141.0),
                GeoPoint(-32.0, 125.0), GeoPoint(-34.0, 115.0), GeoPoint(-22.0, 114.0),
                GeoPoint(-14.0, 126.0), GeoPoint(-12.0, 130.0)
            ),
            // Greenland
            listOf(
                GeoPoint(83.0, -35.0), GeoPoint(80.0, -18.0), GeoPoint(70.0, -22.0),
                GeoPoint(60.0, -44.0), GeoPoint(65.0, -53.0), GeoPoint(76.0, -68.0),
                GeoPoint(82.0, -50.0), GeoPoint(83.0, -35.0)
            )
        )
    }

    fun findCountry(query: String): CountryInfo? {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return null
        return countries.firstOrNull {
            it.name.lowercase() == q ||
                    it.nativeName.lowercase() == q ||
                    it.id.lowercase() == q ||
                    it.capital.lowercase() == q
        } ?: countries.firstOrNull {
            it.name.lowercase().contains(q) ||
                    it.nativeName.lowercase().contains(q) ||
                    it.capital.lowercase().contains(q)
        }
    }

    val majorCities: List<CityLocation> by lazy {
        listOf(
            CityLocation("Islamabad", "PK", 33.6844, 73.0479),
            CityLocation("Karachi", "PK", 24.8607, 67.0011),
            CityLocation("Lahore", "PK", 31.5204, 74.3587),
            CityLocation("Peshawar", "PK", 34.0151, 71.5249),
            CityLocation("Quetta", "PK", 30.1798, 66.9750),
            CityLocation("Multan", "PK", 30.1575, 71.5249),
            CityLocation("Faisalabad", "PK", 31.4504, 73.1350),
            CityLocation("Rawalpindi", "PK", 33.5651, 73.0169),
            CityLocation("Gwadar", "PK", 25.1216, 62.3254),
            CityLocation("Gilgit", "PK", 35.9208, 74.3080),
            CityLocation("Skardu", "PK", 35.2971, 75.6333),
            CityLocation("Dubai", "AE", 25.2048, 55.2708),
            CityLocation("Abu Dhabi", "AE", 24.4539, 54.3773),
            CityLocation("Makkah", "SA", 21.3891, 39.8579),
            CityLocation("Madinah", "SA", 24.5247, 39.5692),
            CityLocation("Riyadh", "SA", 24.7136, 46.6753),
            CityLocation("Jeddah", "SA", 21.4858, 39.1925),
            CityLocation("Istanbul", "TR", 41.0082, 28.9784),
            CityLocation("Ankara", "TR", 39.9334, 32.8597),
            CityLocation("New Delhi", "IN", 28.6139, 77.2090),
            CityLocation("Mumbai", "IN", 19.0760, 72.8777),
            CityLocation("Beijing", "CN", 39.9042, 116.4074),
            CityLocation("Shanghai", "CN", 31.2304, 121.4737),
            CityLocation("Kabul", "AF", 34.5553, 69.2075),
            CityLocation("Tehran", "IR", 35.6892, 51.3890),
            CityLocation("London", "GB", 51.5074, -0.1278),
            CityLocation("Paris", "FR", 48.8566, 2.3522),
            CityLocation("Berlin", "DE", 52.5200, 13.4050),
            CityLocation("Moscow", "RU", 55.7558, 37.6173),
            CityLocation("New York", "US", 40.7128, -74.0060),
            CityLocation("Los Angeles", "US", 34.0522, -118.2437),
            CityLocation("Tokyo", "JP", 35.6762, 139.6503),
            CityLocation("Jakarta", "ID", -6.2088, 106.8456),
            CityLocation("Sydney", "AU", -33.8688, 151.2093),
            CityLocation("Cairo", "EG", 30.0444, 31.2357)
        )
    }

    fun findCity(query: String): CityLocation? {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return null
        return majorCities.firstOrNull { it.name.lowercase() == q }
            ?: majorCities.firstOrNull { it.name.lowercase().contains(q) }
    }

    fun getBorderColor(country: CountryInfo): Long {
        return when (country.id) {
            "PK" -> 0xFF22C55EL // Emerald Green
            "IN" -> 0xFFF59E0BL // Amber / Saffron
            "CN" -> 0xFFEF4444L // Coral Red
            "IR" -> 0xFFA855F7L // Violet
            "RU" -> 0xFF06B6D4L // Cyan Blue
            "AF" -> 0xFFEAB308L // Gold
            "SA" -> 0xFF14B8A6L // Teal
            "EG" -> 0xFFF97316L // Orange
            "SD" -> 0xFF84CC16L // Lime
            "LY" -> 0xFFD97706L // Amber Ochre
            "DZ" -> 0xFF059669L // Dark Emerald
            "NG" -> 0xFF10B981L // Jade Green
            "NO" -> 0xFF38BDF8L // Sky Blue
            "DE" -> 0xFF818CF8L // Indigo
            "FR" -> 0xFFEC4899L // Rose Pink
            "GB" -> 0xFF6366F1L // Purple Blue
            "MN" -> 0xFFFBBF24L // Sun Yellow
            "JP" -> 0xFFF43F5EL // Crimson Rose
            "ID" -> 0xFF2DD4BFL // Turquoise
            "AU" -> 0xFFFB923CL // Tangerine
            "US" -> 0xFF3B82F6L // Royal Blue
            "BR" -> 0xFF4ADE80L // Light Green
            "TR" -> 0xFFE11D48L // Ruby Crimson
            "AE" -> 0xFF22D3EEL // Bright Cyan
            "ZA" -> 0xFFA78BFAL // Lavender
            else -> {
                val palette = listOf(
                    0xFF38BDF8L, 0xFFF59E0BL, 0xFFEC4899L, 0xFF10B981L,
                    0xFFA855F7L, 0xFFF97316L, 0xFF06B6D4L, 0xFF84CC16L,
                    0xFFE11D48L, 0xFF6366F1L, 0xFF14B8A6L, 0xFFFBBF24L,
                    0xFF2DD4BFL, 0xFFD946EFL, 0xFF4ADE80L, 0xFF3B82F6L
                )
                val idx = kotlin.math.abs(country.id.hashCode()) % palette.size
                palette[idx]
            }
        }
    }
}

data class CityLocation(
    val name: String,
    val countryId: String,
    val lat: Double,
    val lon: Double
)


