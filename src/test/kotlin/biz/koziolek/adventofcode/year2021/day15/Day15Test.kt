package biz.koziolek.adventofcode.year2021.day15

import biz.koziolek.adventofcode.Coord
import biz.koziolek.adventofcode.findInput
import biz.koziolek.adventofcode.getHeight
import biz.koziolek.adventofcode.getWidth
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("2021")
internal class Day15Test {

    private val sampleInput = """
        1163751742
        1381373672
        2136511328
        3694931569
        7463417111
        1319128137
        1359912421
        3125421639
        1293138521
        2311944581
    """.trimIndent().split("\n")

    @Test
    fun testParseRiskMap() {
        val riskMap = parseRiskMap(sampleInput)

        assertEquals(10, riskMap.getWidth())
        assertEquals(10, riskMap.getHeight())
        assertEquals(100, riskMap.size)
    }

    @Test
    @Disabled("Broken after adding unidirectional edges support to Graph.findShortestPath()")
    fun testFindLowestRiskPath() {
        val riskMap = parseRiskMap(sampleInput)
        val lowestRiskPath = findLowestRiskPath(riskMap,
            start = Coord(0, 0),
            end = Coord(riskMap.getWidth() - 1, riskMap.getHeight() - 1)
        )
        println(toString(riskMap, lowestRiskPath))

        assertEquals(19, lowestRiskPath.size)
        assertEquals(40, getTotalRisk(riskMap, lowestRiskPath))
    }

    @Test
    @Tag("answer")
    @Disabled("Broken after adding unidirectional edges support to Graph.findShortestPath()")
    fun testAnswer1() {
        val fullInput = findInput(object {}).readLines()
        val riskMap = parseRiskMap(fullInput)
        val lowestRiskPath = findLowestRiskPath(riskMap,
            start = Coord(0, 0),
            end = Coord(riskMap.getWidth() - 1, riskMap.getHeight() - 1)
        )
        assertEquals(410, getTotalRisk(riskMap, lowestRiskPath))
    }

    @Test
    fun testExpandMap() {
        val singleValueMap = mapOf(Coord(0, 0) to 8)
        val expandedSingleValueMap = expandMap(singleValueMap, expandWidth = 14, expandHeight = 4)
        val expectedExpandedSingleValueMap = """
            8 9 1 2 3 4 5 6 7 8 9 1 2 3 4
            9 1 2 3 4 5 6 7 8 9 1 2 3 4 5
            1 2 3 4 5 6 7 8 9 1 2 3 4 5 6
            2 3 4 5 6 7 8 9 1 2 3 4 5 6 7
            3 4 5 6 7 8 9 1 2 3 4 5 6 7 8
        """.trimIndent().replace(" ", "")
        assertEquals(expectedExpandedSingleValueMap, toString(expandedSingleValueMap, path = emptyList()))

        val riskMap = parseRiskMap(sampleInput)

        val expandedMap2By3 = expandMap(riskMap, expandWidth = 2, expandHeight = 1)
        val expectedExpandedMap1By2 = """
            116375174222748628533385973964
            138137367224924847833513595894
            213651132832476224394358733541
            369493156947151426715826253782
            746341711185745282229685639333
            131912813724212392483532341359
            135991242124611235323572234643
            312542163942365327415347643852
            129313852123142496323425351743
            231194458134221556924533266713
            227486285333859739644496184175
            249248478335135958944624616915
            324762243943587335415469844652
            471514267158262537826937364893
            857452822296856393331796741444
            242123924835323413594643452461
            246112353235722346434683345754
            423653274153476438526458754963
            231424963234253517434536462854
            342215569245332667135644377824
        """.trimIndent()
        assertEquals(expectedExpandedMap1By2, toString(expandedMap2By3, path = emptyList()))

        val expandedMap5By5 = expandMap(riskMap, expandWidth = 4, expandHeight = 4)
        val expectedExpandedMap5By5 = """
            11637517422274862853338597396444961841755517295286
            13813736722492484783351359589446246169155735727126
            21365113283247622439435873354154698446526571955763
            36949315694715142671582625378269373648937148475914
            74634171118574528222968563933317967414442817852555
            13191281372421239248353234135946434524615754563572
            13599124212461123532357223464346833457545794456865
            31254216394236532741534764385264587549637569865174
            12931385212314249632342535174345364628545647573965
            23119445813422155692453326671356443778246755488935
            22748628533385973964449618417555172952866628316397
            24924847833513595894462461691557357271266846838237
            32476224394358733541546984465265719557637682166874
            47151426715826253782693736489371484759148259586125
            85745282229685639333179674144428178525553928963666
            24212392483532341359464345246157545635726865674683
            24611235323572234643468334575457944568656815567976
            42365327415347643852645875496375698651748671976285
            23142496323425351743453646285456475739656758684176
            34221556924533266713564437782467554889357866599146
            33859739644496184175551729528666283163977739427418
            35135958944624616915573572712668468382377957949348
            43587335415469844652657195576376821668748793277985
            58262537826937364893714847591482595861259361697236
            96856393331796741444281785255539289636664139174777
            35323413594643452461575456357268656746837976785794
            35722346434683345754579445686568155679767926678187
            53476438526458754963756986517486719762859782187396
            34253517434536462854564757396567586841767869795287
            45332667135644377824675548893578665991468977611257
            44961841755517295286662831639777394274188841538529
            46246169155735727126684683823779579493488168151459
            54698446526571955763768216687487932779859814388196
            69373648937148475914825958612593616972361472718347
            17967414442817852555392896366641391747775241285888
            46434524615754563572686567468379767857948187896815
            46833457545794456865681556797679266781878137789298
            64587549637569865174867197628597821873961893298417
            45364628545647573965675868417678697952878971816398
            56443778246755488935786659914689776112579188722368
            55172952866628316397773942741888415385299952649631
            57357271266846838237795794934881681514599279262561
            65719557637682166874879327798598143881961925499217
            71484759148259586125936169723614727183472583829458
            28178525553928963666413917477752412858886352396999
            57545635726865674683797678579481878968159298917926
            57944568656815567976792667818781377892989248891319
            75698651748671976285978218739618932984172914319528
            56475739656758684176786979528789718163989182927419
            67554889357866599146897761125791887223681299833479
        """.trimIndent()
        assertEquals(expectedExpandedMap5By5, toString(expandedMap5By5, path = emptyList()))
    }

    @Test
    @Tag("answer")
    @Disabled("Broken after adding unidirectional edges support to Graph.findShortestPath()")
    fun testAnswer2() {
        val fullInput = findInput(object {}).readLines()
        val riskMap = parseRiskMap(fullInput)
        val expandedMap = expandMap(riskMap, expandWidth = 4, expandHeight = 4)
        val lowestRiskPath = findLowestRiskPath(expandedMap,
            start = Coord(0, 0),
            end = Coord(expandedMap.getWidth() - 1, expandedMap.getHeight() - 1)
        )
        assertEquals(2809, getTotalRisk(expandedMap, lowestRiskPath))
    }
}
