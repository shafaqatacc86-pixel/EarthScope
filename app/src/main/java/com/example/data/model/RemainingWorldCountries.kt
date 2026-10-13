package com.example.data.model

object RemainingWorldCountries {

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
        desc: String
    ): CountryInfo {
        return AdditionalWorldCountries.createCountry(
            id = id,
            name = name,
            nativeName = nativeName,
            capital = capital,
            continent = continent,
            region = region,
            pop = pop,
            area = area,
            currency = currency,
            langs = langs,
            flag = flag,
            cLat = cLat,
            cLon = cLon,
            capLat = capLat,
            capLon = capLon,
            tz = tz,
            desc = desc
        )
    }

    val list: List<CountryInfo> by lazy {
        listOf(
            // ================= REMAINING EUROPE & CAUCASUS / MEDITERRANEAN =================
            c("CY", "Cyprus", "Κύπρος / Kıbrıs", "Nicosia", "Europe", "Mediterranean", 1260138L, 9251.0, "Euro (EUR)", listOf("Greek", "Turkish"), "🇨🇾", 35.1264, 33.4299, 35.1856, 33.3823, 2.0, "Eastern Mediterranean island nation known for ancient ruins, sunny beaches, and the Troodos Mountains."),
            c("LU", "Luxembourg", "Lëtzebuerg", "Luxembourg City", "Europe", "Western Europe", 660809L, 2586.0, "Euro (EUR)", listOf("Luxembourgish", "French", "German"), "🇱🇺", 49.8153, 6.1296, 49.6116, 6.1319, 1.0, "Small European nation surrounded by Belgium, France, and Germany, famed for medieval castles and finance."),
            c("MT", "Malta", "Malta", "Valletta", "Europe", "Southern Europe", 542051L, 316.0, "Euro (EUR)", listOf("Maltese", "English"), "🇲🇹", 35.9375, 14.3754, 35.8989, 14.5146, 1.0, "Central Mediterranean archipelago known for historic fortresses, megalithic temples, and Blue Lagoon."),
            c("ME", "Montenegro", "Crna Gora", "Podgorica", "Europe", "Balkans", 616177L, 13812.0, "Euro (EUR)", listOf("Montenegrin"), "🇲🇪", 42.7087, 19.3744, 42.4304, 19.2594, 1.0, "Balkan country with rugged mountains, medieval villages, and the scenic Bay of Kotor along the Adriatic Sea."),
            c("XK", "Kosovo", "Kosova", "Pristina", "Europe", "Balkans", 1761985L, 10887.0, "Euro (EUR)", listOf("Albanian", "Serbian"), "🇽🇰", 42.6026, 20.9030, 42.6629, 21.1655, 1.0, "Landlocked country in the central Balkans surrounded by the Accursed Mountains and Sharr peaks."),
            c("AD", "Andorra", "Andorra", "Andorra la Vella", "Europe", "Southern Europe", 80088L, 468.0, "Euro (EUR)", listOf("Catalan"), "🇦🇩", 42.5462, 1.6016, 42.5063, 1.5218, 1.0, "Principality high in the Pyrenees mountains between France and Spain, renowned for alpine ski resorts."),
            c("MC", "Monaco", "Monaco", "Monaco", "Europe", "Western Europe", 39050L, 2.02, "Euro (EUR)", listOf("French"), "🇲🇨", 43.7384, 7.4246, 43.7384, 7.4246, 1.0, "Sovereign city-state on the French Riviera famed for Monte Carlo, Grand Prix racing, and yacht harbors."),
            c("LI", "Liechtenstein", "Liechtenstein", "Vaduz", "Europe", "Central Europe", 39584L, 160.0, "Swiss Franc (CHF)", listOf("German"), "🇱🇮", 47.1660, 9.5554, 47.1410, 9.5209, 1.0, "German-speaking alpine principality situated between Austria and Switzerland along the Rhine."),
            c("SM", "San Marino", "San Marino", "San Marino", "Europe", "Southern Europe", 33642L, 61.2, "Euro (EUR)", listOf("Italian"), "🇸🇲", 43.9424, 12.4578, 43.9356, 12.4473, 1.0, "Mountainous microstate surrounded by north-central Italy, among the world's oldest republics."),
            c("VA", "Vatican City", "Città del Vaticano", "Vatican City", "Europe", "Southern Europe", 825L, 0.49, "Euro (EUR)", listOf("Italian", "Latin"), "🇻🇦", 41.9029, 12.4534, 41.9029, 12.4534, 1.0, "Independent city-state enclaved within Rome, Italy, home to St. Peter's Basilica and the Sistine Chapel."),

            // ================= REMAINING AFRICA (ALL 54 SOVEREIGN AFRICAN COUNTRIES) =================
            c("ER", "Eritrea", "ኤርትራ / إرتريا", "Asmara", "Africa", "East Africa", 3748901L, 117600.0, "Eritrean Nakfa (ERN)", listOf("Tigrinya", "Arabic", "English"), "🇪🇷", 15.1794, 39.7823, 15.3229, 38.9251, 3.0, "Northeast African country on the Red Sea coast, known for Italian modernist architecture in Asmara and Dahlak Islands."),
            c("DJ", "Djibouti", "جيبوتي / Djibouti", "Djibouti", "Africa", "East Africa", 1136455L, 23200.0, "Djiboutian Franc (DJF)", listOf("French", "Arabic"), "🇩🇯", 11.8251, 42.5903, 11.5721, 43.1456, 3.0, "Situated in the Horn of Africa at the Bab-el-Mandeb strait connecting the Red Sea and Gulf of Aden."),
            c("RW", "Rwanda", "Rwanda", "Kigali", "Africa", "East Africa", 14094683L, 26338.0, "Rwandan Franc (RWF)", listOf("Kinyarwanda", "English", "French"), "🇷🇼", -1.9403, 29.8739, -1.9441, 30.0619, 2.0, "Known as the Land of a Thousand Hills, famed for Volcanoes National Park, mountain gorillas, and clean Kigali."),
            c("BI", "Burundi", "Burundi", "Gitega", "Africa", "East Africa", 13238559L, 27834.0, "Burundian Franc (BIF)", listOf("Kirundi", "French"), "🇧🇮", -3.3731, 29.9189, -3.4271, 29.9246, 2.0, "East-Central African country bordering Lake Tanganyika, known for royal drumming traditions and lush hills."),
            c("MW", "Malawi", "Malawi", "Lilongwe", "Africa", "Southeastern Africa", 20931751L, 118484.0, "Malawian Kwacha (MWK)", listOf("English", "Chichewa"), "🇲🇼", -13.2543, 34.3015, -13.9626, 33.7741, 2.0, "Warm Heart of Africa defined by Rift Valley topography and the crystal waters of Lake Malawi."),
            c("CG", "Republic of the Congo", "République du Congo", "Brazzaville", "Africa", "Central Africa", 6106869L, 342000.0, "Central African CFA Franc (XAF)", listOf("French"), "🇨🇬", -0.2280, 15.8277, -4.2634, 15.2429, 1.0, "Central African nation with tropical rainforest reserves and its capital Brazzaville across the Congo River from Kinshasa."),
            c("CF", "Central African Republic", "Centrafrique", "Bangui", "Africa", "Central Africa", 5742315L, 622984.0, "Central African CFA Franc (XAF)", listOf("French", "Sango"), "🇨🇫", 6.6111, 20.9394, 4.3947, 18.5582, 1.0, "Landlocked country in the heart of Africa known for Dzanga-Sangha rainforest reserves and wildlife."),
            c("GA", "Gabon", "Gabon", "Libreville", "Africa", "Central Africa", 2436566L, 267668.0, "Central African CFA Franc (XAF)", listOf("French"), "🇬🇦", -0.8037, 11.6094, 0.4162, 9.4673, 1.0, "Along the Atlantic coast of Central Africa, with protected national parks covering tropical rainforest."),
            c("GQ", "Equatorial Guinea", "Guinea Ecuatorial", "Malabo", "Africa", "Central Africa", 1714671L, 28051.0, "Central African CFA Franc (XAF)", listOf("Spanish", "French", "Portuguese"), "🇬🇶", 1.6508, 10.2679, 3.7504, 8.7371, 1.0, "Central African country comprising the Rio Muni mainland and Bioko Island where the capital Malabo sits."),
            c("ST", "São Tomé and Príncipe", "São Tomé e Príncipe", "São Tomé", "Africa", "Central Africa", 231856L, 964.0, "Dobra (STN)", listOf("Portuguese"), "🇸🇹", 0.1864, 6.6131, 0.3365, 6.7273, 0.0, "African island nation in the Gulf of Guinea close to the equator, known for volcanic peaks and cocoa."),
            c("BF", "Burkina Faso", "Burkina Faso", "Ouagadougou", "Africa", "West Africa", 23251485L, 274200.0, "West African CFA Franc (XOF)", listOf("French", "Mooré"), "🇧🇫", 12.2383, -1.5616, 12.3714, -1.5197, 0.0, "Landlocked West African nation celebrated for Pan-African cinema FESPACO and cultural crafts."),
            c("BJ", "Benin", "Bénin", "Porto-Novo", "Africa", "West Africa", 13712828L, 114763.0, "West African CFA Franc (XOF)", listOf("French"), "🇧🇯", 9.3077, 2.3158, 6.4969, 2.6289, 1.0, "French-speaking West African nation, home to Royal Palaces of Abomey and Pendjari National Park."),
            c("TG", "Togo", "Togo", "Lomé", "Africa", "West Africa", 9053799L, 56785.0, "West African CFA Franc (XOF)", listOf("French"), "🇹🇬", 8.6195, 0.8248, 6.1256, 1.2254, 0.0, "West African nation on the Gulf of Guinea known for palm-lined beaches and hilltop villages."),
            c("GN", "Guinea", "Guinée", "Conakry", "Africa", "West Africa", 14190612L, 245857.0, "Guinean Franc (GNF)", listOf("French"), "🇬🇳", 9.9456, -9.6966, 9.6412, -13.5784, 0.0, "West African country bordered by the Atlantic Ocean, known for the Fouta Djallon highlands, source of West Africa's major rivers."),
            c("SL", "Sierra Leone", "Sierra Leone", "Freetown", "Africa", "West Africa", 8791092L, 71740.0, "Sierra Leonean Leone (SLE)", listOf("English", "Krio"), "🇸🇱", 8.4606, -11.7799, 8.4657, -13.2317, 0.0, "West African country on the Atlantic Ocean known for white-sand beaches on the Freetown Peninsula."),
            c("LR", "Liberia", "Liberia", "Monrovia", "Africa", "West Africa", 5418377L, 111369.0, "Liberian Dollar (LRD)", listOf("English"), "🇱🇷", 6.4281, -9.4295, 6.3156, -10.8074, 0.0, "West African nation along the Atlantic coast, home to Sapo National Park rainforest."),
            c("GW", "Guinea-Bissau", "Guiné-Bissau", "Bissau", "Africa", "West Africa", 2150842L, 36125.0, "West African CFA Franc (XOF)", listOf("Portuguese"), "🇬🇼", 11.8037, -15.1804, 11.8636, -15.5977, 0.0, "Tropical country on West Africa's Atlantic coast, famous for the biodiverse Bijagós Archipelago."),
            c("GM", "Gambia", "The Gambia", "Banjul", "Africa", "West Africa", 2773168L, 11295.0, "Gambian Dalasi (GMD)", listOf("English"), "🇬🇲", 13.4432, -15.3101, 13.4549, -16.5790, 0.0, "Smallest country within mainland Africa, surrounded by Senegal along the lower Gambia River."),
            c("CV", "Cape Verde", "Cabo Verde", "Praia", "Africa", "West Africa", 598682L, 4033.0, "Cape Verdean Escudo (CVE)", listOf("Portuguese"), "🇨🇻", 16.0021, -24.0132, 14.9330, -23.5133, -1.0, "Volcanic archipelago off the northwest coast of Africa, known for Creole Portuguese-African culture and beaches."),
            c("LS", "Lesotho", "Lesotho", "Maseru", "Africa", "Southern Africa", 2330318L, 30355.0, "Lesotho Loti (LSL)", listOf("Sesotho", "English"), "🇱🇸", -29.6100, 28.2336, -29.3151, 27.4869, 2.0, "High-altitude, landlocked Mountain Kingdom completely encircled by South Africa."),
            c("SZ", "Eswatini", "eSwatini", "Mbabane", "Africa", "Southern Africa", 1210822L, 17364.0, "Lilangeni (SZL)", listOf("Swazi", "English"), "🇸🇿", -26.5225, 31.4659, -26.3054, 31.1367, 2.0, "Monarchy in southern Africa bordered by South Africa and Mozambique, known for wilderness reserves."),
            c("MU", "Mauritius", "Maurice", "Port Louis", "Africa", "Indian Ocean", 1300557L, 2040.0, "Mauritian Rupee (MUR)", listOf("English", "French", "Creole"), "🇲🇺", -20.3484, 57.5522, -20.1609, 57.5012, 4.0, "Indian Ocean island nation known for lagoons, reefs, Chamarel seven-colored earths, and Le Morne Brabant."),
            c("SC", "Seychelles", "Sesel", "Victoria", "Africa", "Indian Ocean", 107660L, 459.0, "Seychellois Rupee (SCR)", listOf("English", "French", "Seychellois Creole"), "🇸🇨", -4.6796, 55.4920, -4.6191, 55.4513, 4.0, "Archipelago of 115 islands in the Indian Ocean off East Africa, famous for granite boulder beaches and giant tortoises."),
            c("KM", "Comoros", "جزر القمر / Comores", "Moroni", "Africa", "Indian Ocean", 852075L, 1862.0, "Comorian Franc (KMF)", listOf("Comorian", "Arabic", "French"), "🇰🇲", -11.6455, 43.3333, -11.7172, 43.2473, 3.0, "Volcanic archipelago off Africa's east coast in the warm Indian Ocean waters of the Mozambique Channel."),
            c("EH", "Western Sahara", "الصحراء الغربية", "Laayoune", "Africa", "North Africa", 587259L, 266000.0, "MAD", listOf("Arabic"), "🇪🇭", 24.2155, -12.8858, 27.1536, -13.2033, 1.0, "Arid Atlantic coastal territory in northwest Africa consisting of desert flats and rocky highlands."),

            // ================= REMAINING CENTRAL AMERICA & CARIBBEAN =================
            c("HN", "Honduras", "Honduras", "Tegucigalpa", "North America", "Central America", 10593798L, 112492.0, "Honduran Lempira (HNL)", listOf("Spanish"), "🇭🇳", 15.2000, -86.2419, 14.0723, -87.1921, -6.0, "Central American country with Mayan ruins at Copán and Caribbean Bay Islands coral reefs."),
            c("NI", "Nicaragua", "Nicaragua", "Managua", "North America", "Central America", 7046310L, 130373.0, "Nicaraguan Córdoba (NIO)", listOf("Spanish"), "🇳🇮", 12.8654, -85.2072, 12.1150, -86.2362, -6.0, "Set between the Pacific Ocean and the Caribbean Sea, known for dramatic terrain of lakes, volcanoes, and beaches."),
            c("SV", "El Salvador", "El Salvador", "San Salvador", "North America", "Central America", 6364943L, 21041.0, "US Dollar (USD)", listOf("Spanish"), "🇸🇻", 13.7942, -88.8965, 13.6929, -89.2182, -6.0, "Smallest and most densely populated Central American nation, known for Pacific surf beaches and volcanic peaks."),
            c("BZ", "Belize", "Belize", "Belmopan", "North America", "Central America", 410825L, 22966.0, "Belize Dollar (BZD)", listOf("English", "Spanish"), "🇧🇿", 17.1899, -88.4976, 17.2510, -88.7590, -6.0, "On the eastern coast of Central America, with the Belize Barrier Reef, Great Blue Hole, and Mayan ruins."),
            c("DO", "Dominican Republic", "República Dominicana", "Santo Domingo", "North America", "Caribbean", 11332972L, 48671.0, "Dominican Peso (DOP)", listOf("Spanish"), "🇩🇴", 18.7357, -70.1627, 18.4861, -69.9312, -4.0, "Caribbean nation sharing the island of Hispaniola with Haiti, famed for beaches, resorts, and Pico Duarte."),
            c("HT", "Haiti", "Haïti", "Port-au-Prince", "North America", "Caribbean", 11724763L, 27750.0, "Haitian Gourde (HTG)", listOf("French", "Haitian Creole"), "🇭🇹", 18.9712, -72.2852, 18.5944, -72.3074, -5.0, "Caribbean country on the western third of Hispaniola, home to the historic Citadelle Laferrière."),
            c("JM", "Jamaica", "Jamaica", "Kingston", "North America", "Caribbean", 2825544L, 10991.0, "Jamaican Dollar (JMD)", listOf("English"), "🇯🇲", 18.1096, -77.2975, 17.9714, -76.7936, -5.0, "Caribbean island nation with Blue Mountains, rainforests, reef-lined beaches, and the birthplace of reggae."),
            c("TT", "Trinidad and Tobago", "Trinidad and Tobago", "Port of Spain", "North America", "Caribbean", 1534937L, 5130.0, "Trinidad and Tobago Dollar (TTD)", listOf("English"), "🇹🇹", 10.6918, -61.2225, 10.6549, -61.5019, -4.0, "Dual-island Caribbean nation near Venezuela, known for Carnival, calypso, steelpan music, and Pitch Lake."),
            c("BS", "Bahamas", "The Bahamas", "Nassau", "North America", "Caribbean", 412623L, 13943.0, "Bahamian Dollar (BSD)", listOf("English"), "🇧🇸", 25.0343, -77.3963, 25.0443, -77.3504, -5.0, "Lucayan Archipelago of over 700 coral islands and cays in the Atlantic Ocean north of Cuba."),
            c("BB", "Barbados", "Barbados", "Bridgetown", "North America", "Caribbean", 281995L, 430.0, "Barbadian Dollar (BBD)", listOf("English"), "🇧🇧", 13.1939, -59.5432, 13.1132, -59.5988, -4.0, "Eastern Caribbean island known for botanical gardens, Harrison's Cave, and coral-sand beaches."),
            c("LC", "Saint Lucia", "Saint Lucia", "Castries", "North America", "Caribbean", 180251L, 616.0, "East Caribbean Dollar (XCD)", listOf("English"), "🇱🇨", 13.9094, -60.9789, 14.0101, -60.9875, -4.0, "Eastern Caribbean island nation with the dramatic twin volcanic spires of the Pitons."),
            c("GD", "Grenada", "Grenada", "St. George's", "North America", "Caribbean", 126183L, 344.0, "East Caribbean Dollar (XCD)", listOf("English"), "🇬🇩", 12.1165, -61.6790, 12.0561, -61.7488, -4.0, "Known as the 'Island of Spice' in the southeastern Caribbean for its nutmeg and mace plantations."),
            c("VC", "Saint Vincent and the Grenadines", "Saint Vincent", "Kingstown", "North America", "Caribbean", 103698L, 389.0, "East Caribbean Dollar (XCD)", listOf("English"), "🇻🇨", 12.9843, -61.2872, 13.1600, -61.2248, -4.0, "Southern Caribbean island chain of volcanic Saint Vincent and the coral islands of the Grenadines."),
            c("AG", "Antigua and Barbuda", "Antigua and Barbuda", "St. John's", "North America", "Caribbean", 94298L, 442.0, "East Caribbean Dollar (XCD)", listOf("English"), "🇦🇬", 17.0608, -61.7964, 17.1274, -61.8468, -4.0, "Twin-island country between the Caribbean Sea and the Atlantic, famous for 365 reef-lined beaches."),
            c("DM", "Dominica", "Dominica", "Roseau", "North America", "Caribbean", 73040L, 751.0, "East Caribbean Dollar (XCD)", listOf("English"), "🇩🇲", 15.4150, -61.3710, 15.3092, -61.3794, -4.0, "Mountainous Caribbean island known as the 'Nature Island' for natural hot springs, Boiling Lake, and rainforests."),
            c("KN", "Saint Kitts and Nevis", "Saint Kitts and Nevis", "Basseterre", "North America", "Caribbean", 47755L, 261.0, "East Caribbean Dollar (XCD)", listOf("English"), "🇰🇳", 17.3578, -62.7830, 17.3026, -62.7177, -4.0, "Dual-island nation situated between the Atlantic Ocean and Caribbean Sea, known for cloud-shrouded mountains."),
            c("PR", "Puerto Rico", "Puerto Rico", "San Juan", "North America", "Caribbean", 3260314L, 9104.0, "US Dollar (USD)", listOf("Spanish", "English"), "🇵🇷", 18.2208, -66.5901, 18.4655, -66.1057, -4.0, "Caribbean island featuring El Yunque tropical rainforest, historic Old San Juan fortresses, and bioluminescent bays."),

            // ================= REMAINING OCEANIA & PACIFIC ISLANDS =================
            c("SB", "Solomon Islands", "Solomon Islands", "Honiara", "Oceania", "Melanesia", 740424L, 28896.0, "Solomon Islands Dollar (SBD)", listOf("English"), "🇸🇧", -9.6457, 160.1562, -9.4456, 159.9729, 11.0, "Nation of hundreds of islands in the South Pacific, rich in WWII history and coral reefs."),
            c("VU", "Vanuatu", "Vanuatu", "Port Vila", "Oceania", "Melanesia", 334506L, 12189.0, "Vanuatu Vatu (VUV)", listOf("Bislama", "English", "French"), "🇻🇺", -15.3767, 166.9592, -17.7333, 168.3273, 11.0, "South Pacific Ocean nation made up of roughly 80 islands, active Mount Yasur volcano, and diving reefs."),
            c("WS", "Samoa", "Sāmoa", "Apia", "Oceania", "Polynesia", 225681L, 2842.0, "Samoan Tālā (WST)", listOf("Samoan", "English"), "🇼🇸", -13.7590, -172.1046, -13.8333, -171.7667, 13.0, "Polynesian island country consisting of Upolu and Savai'i with reef-bordered lagoons and waterfalls."),
            c("TO", "Tonga", "Tonga", "Nuku'alofa", "Oceania", "Polynesia", 107773L, 747.0, "Tongan Paʻanga (TOP)", listOf("Tongan", "English"), "🇹🇴", -21.1790, -175.1982, -21.1393, -175.2049, 13.0, "Polynesian kingdom of more than 170 South Pacific islands, many uninhabited and lined in white beaches."),
            c("KI", "Kiribati", "Kiribati", "Tarawa", "Oceania", "Micronesia", 133515L, 811.0, "Australian Dollar (AUD)", listOf("English", "Gilbertese"), "🇰🇮", 1.8709, -157.3626, 1.4518, 172.9717, 12.0, "Central Pacific island country straddling the equator and the International Date Line across 33 atolls."),
            c("FM", "Micronesia", "Federated States of Micronesia", "Palikir", "Oceania", "Micronesia", 115224L, 702.0, "US Dollar (USD)", listOf("English"), "🇫🇲", 7.4256, 150.5508, 6.9147, 158.1610, 11.0, "Spread across the western Pacific Ocean comprising more than 600 islands, including Pohnpei and Chuuk Lagoon."),
            c("MH", "Marshall Islands", "Marshall Islands", "Majuro", "Oceania", "Micronesia", 41996L, 181.0, "US Dollar (USD)", listOf("Marshallese", "English"), "🇲🇭", 7.1315, 171.1845, 7.0897, 171.3803, 12.0, "Sprawling chain of volcanic islands and coral atolls in the central Pacific Ocean, between Hawaii and the Philippines."),
            c("PW", "Palau", "Belau", "Ngerulmud", "Oceania", "Micronesia", 18058L, 459.0, "US Dollar (USD)", listOf("Palauan", "English"), "🇵🇼", 7.5150, 134.5825, 7.5004, 134.6243, 9.0, "Archipelago of over 500 islands in the western Pacific Ocean, famed for the mushroom-shaped Rock Islands."),
            c("NR", "Nauru", "Naoero", "Yaren", "Oceania", "Micronesia", 12780L, 21.0, "Australian Dollar (AUD)", listOf("Nauruan", "English"), "🇳🇷", -0.5228, 166.9315, -0.5467, 166.9211, 12.0, "Tiny oval-shaped island country in Micronesia, northeast of Australia, ringed by a coral reef."),
            c("TV", "Tuvalu", "Tuvalu", "Funafuti", "Oceania", "Polynesia", 11396L, 26.0, "Australian Dollar (AUD)", listOf("Tuvaluan", "English"), "🇹🇻", -7.1095, 177.6493, -8.5211, 179.1962, 12.0, "South Pacific island nation made up of 9 coral atolls and reef islands midway between Hawaii and Australia."),
            c("AQ", "Antarctica", "Antarctica", "Amundsen-Scott / McMurdo", "Antarctica", "Polar South", 1106L, 14200000.0, "None (Scientific Treaty)", listOf("English", "Russian", "Spanish"), "🇦🇶", -78.0000, 15.0000, -77.8419, 166.6863, 0.0, "Earth's southernmost continent containing the geographic South Pole, covered by an immense polar ice sheet and international research stations.")
        )
    }
}
