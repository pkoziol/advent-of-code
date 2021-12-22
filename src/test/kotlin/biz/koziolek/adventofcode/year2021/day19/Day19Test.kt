package biz.koziolek.adventofcode.year2021.day19

import biz.koziolek.adventofcode.Coord3d
import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import kotlin.test.assertContains

@Tag("2021")
internal class Day19Test {

    private val sampleReport2d = """
            --- scanner 0 ---
            0,2,0
            4,1,0
            3,3,0

            --- scanner 1 ---
            -1,-1,0
            -5,0,0
            -2,1,0
        """.trimIndent().split("\n")

    private val sampleReport = """
        --- scanner 0 ---
        404,-588,-901
        528,-643,409
        -838,591,734
        390,-675,-793
        -537,-823,-458
        -485,-357,347
        -345,-311,381
        -661,-816,-575
        -876,649,763
        -618,-824,-621
        553,345,-567
        474,580,667
        -447,-329,318
        -584,868,-557
        544,-627,-890
        564,392,-477
        455,729,728
        -892,524,684
        -689,845,-530
        423,-701,434
        7,-33,-71
        630,319,-379
        443,580,662
        -789,900,-551
        459,-707,401

        --- scanner 1 ---
        686,422,578
        605,423,415
        515,917,-361
        -336,658,858
        95,138,22
        -476,619,847
        -340,-569,-846
        567,-361,727
        -460,603,-452
        669,-402,600
        729,430,532
        -500,-761,534
        -322,571,750
        -466,-666,-811
        -429,-592,574
        -355,545,-477
        703,-491,-529
        -328,-685,520
        413,935,-424
        -391,539,-444
        586,-435,557
        -364,-763,-893
        807,-499,-711
        755,-354,-619
        553,889,-390

        --- scanner 2 ---
        649,640,665
        682,-795,504
        -784,533,-524
        -644,584,-595
        -588,-843,648
        -30,6,44
        -674,560,763
        500,723,-460
        609,671,-379
        -555,-800,653
        -675,-892,-343
        697,-426,-610
        578,704,681
        493,664,-388
        -671,-858,530
        -667,343,800
        571,-461,-707
        -138,-166,112
        -889,563,-600
        646,-828,498
        640,759,510
        -630,509,768
        -681,-892,-333
        673,-379,-804
        -742,-814,-386
        577,-820,562

        --- scanner 3 ---
        -589,542,597
        605,-692,669
        -500,565,-823
        -660,373,557
        -458,-679,-417
        -488,449,543
        -626,468,-788
        338,-750,-386
        528,-832,-391
        562,-778,733
        -938,-730,414
        543,643,-506
        -524,371,-870
        407,773,750
        -104,29,83
        378,-903,-323
        -778,-728,485
        426,699,580
        -438,-605,-362
        -469,-447,-387
        509,732,623
        647,635,-688
        -868,-804,481
        614,-800,639
        595,780,-596

        --- scanner 4 ---
        727,592,562
        -293,-554,779
        441,611,-461
        -714,465,-776
        -743,427,-804
        -660,-479,-426
        832,-632,460
        927,-485,-438
        408,393,-506
        466,436,-512
        110,16,151
        -258,-428,682
        -393,719,612
        -211,-452,876
        808,-476,-593
        -575,615,604
        -485,667,467
        -680,325,-822
        -627,-443,-432
        872,-547,-609
        833,512,582
        807,604,487
        839,-516,451
        891,-625,532
        -652,-548,-490
        30,-46,-14
    """.trimIndent().split("\n")

    @Test
    fun testParseReport() {
        val scanners = parseScannerReport(sampleReport)
        assertEquals(5, scanners.size)

        assertEquals(25, scanners[0].beacons.size)
        assertContains(scanners[0].beacons, Coord3d(404, -588, -901))

        assertEquals(25, scanners[1].beacons.size)
        assertContains(scanners[1].beacons, Coord3d(686, 422, 578))

        assertEquals(26, scanners[2].beacons.size)
        assertContains(scanners[2].beacons, Coord3d(649, 640, 665))

        assertEquals(25, scanners[3].beacons.size)
        assertContains(scanners[3].beacons, Coord3d(-589, 542, 597))

        assertEquals(26, scanners[4].beacons.size)
        assertContains(scanners[4].beacons, Coord3d(727, 592, 562))
    }

    @Test
    fun testFindSameCoords2d() {
        val scanners = parseScannerReport(sampleReport2d)
        val sameCoords12 = findSameCoords(scanners[0], scanners[1])
        assertEquals(0, sameCoords12.size)

        val sameCoords3 = findSameCoords(scanners[0], scanners[1], minInCommon = 3)
        val expected = mapOf(
            Coord3d(0, 2, 0) to Coord3d(-5, 0, 0),
            Coord3d(4, 1, 0) to Coord3d(-1, -1, 0),
            Coord3d(3, 3, 0) to Coord3d(-2, 1, 0),
        )
        assertEquals(expected, sameCoords3)
    }

    @Test
    fun testFindSameCoords3d() {
        val scanners = parseScannerReport(sampleReport)

        val sameCoords0and1 = findSameCoords(scanners[0], scanners[1])
        val expected0and1 = mapOf(
            Coord3d(-618,-824,-621) to Coord3d(686,422,578),
            Coord3d(-537,-823,-458) to Coord3d(605,423,415),
            Coord3d(-447,-329,318) to Coord3d(515,917,-361),
            Coord3d(404,-588,-901) to Coord3d(-336,658,858),
            Coord3d(544,-627,-890) to Coord3d(-476,619,847),
            Coord3d(528,-643,409) to Coord3d(-460,603,-452),
            Coord3d(-661,-816,-575) to Coord3d(729,430,532),
            Coord3d(390,-675,-793) to Coord3d(-322,571,750),
            Coord3d(423,-701,434) to Coord3d(-355,545,-477),
            Coord3d(-345,-311,381) to Coord3d(413,935,-424),
            Coord3d(459,-707,401) to Coord3d(-391,539,-444),
            Coord3d(-485,-357,347) to Coord3d(553,889,-390),
        )
        assertEquals(expected0and1, sameCoords0and1)
    }

    @Test
    fun testFindSecondScannerRotation() {
        val scanners = parseScannerReport(sampleReport)

        val sameCoords0and1 = findSameCoords(scanners[0], scanners[1])
        val scanner1Rotation = findSecondScannerRotation(sameCoords0and1)
        assertEquals(Rotation("-x", "+y", "-z"), scanner1Rotation)

        val sameCoords1and4 = findSameCoords(scanners[1], scanners[4])
        val scanner4Rotation = findSecondScannerRotation(sameCoords1and4)
        assertEquals(Rotation("+y", "-z", "-x"), scanner4Rotation)
    }

    @Test
    fun testFindSecondScannerCoordRelativeToFirst() {
        val scanners = parseScannerReport(sampleReport)
        val sameCoords0and1 = findSameCoords(scanners[0], scanners[1])
        val scanner1Coord = findSecondScannerCoordRelativeToFirst(sameCoords0and1)
        assertEquals(Coord3d(68, -1246, -43), scanner1Coord)
    }

    @Test
    fun testConvertCoordsRelativeToOther() {
        val scanners = parseScannerReport(sampleReport)

        val sameCoords0and1 = findSameCoords(scanners[0], scanners[1])
        val scanner1Rotation = findSecondScannerRotation(sameCoords0and1)
        val scanner1Coord = findSecondScannerCoordRelativeToFirst(sameCoords0and1)
        assertEquals(Coord3d(68, -1246, -43), scanner1Coord)

        val normalizedScanner1 = scanners[1].normalize(scanner1Rotation, scanner1Coord)
        val sameCoords0andNormalized1 = findSameCoords(scanners[0], normalizedScanner1)
        assertEquals(sameCoords0and1.keys, sameCoords0andNormalized1.keys, "Scanner 0 coords should not change")
        assertEquals(sameCoords0andNormalized1.keys, sameCoords0andNormalized1.values.toSet(), "Scanner 1 coords after normalization should be the same as 0")

        val sameCoordsNormalized1and4 = findSameCoords(normalizedScanner1, scanners[4])
        val scanner4Rotation = findSecondScannerRotation(sameCoordsNormalized1and4)
        val scanner4Coord = findSecondScannerCoordRelativeToFirst(sameCoordsNormalized1and4)
        assertEquals(Coord3d(-20, -1133, 1061), scanner4Coord)

        val normalizedScanner4 = scanners[4].normalize(scanner4Rotation, scanner4Coord)
        val sameCoordsNormalized1AndNormalized4 = findSameCoords(normalizedScanner1, normalizedScanner4)
        assertEquals(sameCoordsNormalized1and4.keys, sameCoordsNormalized1AndNormalized4.keys, "Scanner 1 coords should not change")
        assertEquals(sameCoordsNormalized1AndNormalized4.keys, sameCoordsNormalized1AndNormalized4.values.toSet(), "Scanner 4 coords after normalization should be the same as 1")
        assertEquals(
            setOf(
                Coord3d(459, -707, 401),
                Coord3d(-739, -1745, 668),
                Coord3d(-485, -357, 347),
                Coord3d(432, -2009, 850),
                Coord3d(528, -643, 409),
                Coord3d(423, -701, 434),
                Coord3d(-345, -311, 381),
                Coord3d(408, -1815, 803),
                Coord3d(534, -1912, 768),
                Coord3d(-687, -1600, 576),
                Coord3d(-447, -329, 318),
                Coord3d(-635, -1737, 486),
            ),
            sameCoordsNormalized1AndNormalized4.values.toSet()
        )
    }

    @Test
    fun testFindAllBeaconsRelativeTo0() {
        val scanners = parseScannerReport(sampleReport)
        val allBeacons = findAllBeaconsRelativeTo0(scanners)
        assertEquals(
            setOf(
                Coord3d(-892, 524, 684),
                Coord3d(-876, 649, 763),
                Coord3d(-838, 591, 734),
                Coord3d(-789, 900, -551),
                Coord3d(-739, -1745, 668),
                Coord3d(-706, -3180, -659),
                Coord3d(-697, -3072, -689),
                Coord3d(-689, 845, -530),
                Coord3d(-687, -1600, 576),
                Coord3d(-661, -816, -575),
                Coord3d(-654, -3158, -753),
                Coord3d(-635, -1737, 486),
                Coord3d(-631, -672, 1502),
                Coord3d(-624, -1620, 1868),
                Coord3d(-620, -3212, 371),
                Coord3d(-618, -824, -621),
                Coord3d(-612, -1695, 1788),
                Coord3d(-601, -1648, -643),
                Coord3d(-584, 868, -557),
                Coord3d(-537, -823, -458),
                Coord3d(-532, -1715, 1894),
                Coord3d(-518, -1681, -600),
                Coord3d(-499, -1607, -770),
                Coord3d(-485, -357, 347),
                Coord3d(-470, -3283, 303),
                Coord3d(-456, -621, 1527),
                Coord3d(-447, -329, 318),
                Coord3d(-430, -3130, 366),
                Coord3d(-413, -627, 1469),
                Coord3d(-345, -311, 381),
                Coord3d(-36, -1284, 1171),
                Coord3d(-27, -1108, -65),
                Coord3d(7, -33, -71),
                Coord3d(12, -2351, -103),
                Coord3d(26, -1119, 1091),
                Coord3d(346, -2985, 342),
                Coord3d(366, -3059, 397),
                Coord3d(377, -2827, 367),
                Coord3d(390, -675, -793),
                Coord3d(396, -1931, -563),
                Coord3d(404, -588, -901),
                Coord3d(408, -1815, 803),
                Coord3d(423, -701, 434),
                Coord3d(432, -2009, 850),
                Coord3d(443, 580, 662),
                Coord3d(455, 729, 728),
                Coord3d(456, -540, 1869),
                Coord3d(459, -707, 401),
                Coord3d(465, -695, 1988),
                Coord3d(474, 580, 667),
                Coord3d(496, -1584, 1900),
                Coord3d(497, -1838, -617),
                Coord3d(527, -524, 1933),
                Coord3d(528, -643, 409),
                Coord3d(534, -1912, 768),
                Coord3d(544, -627, -890),
                Coord3d(553, 345, -567),
                Coord3d(564, 392, -477),
                Coord3d(568, -2007, -577),
                Coord3d(605, -1665, 1952),
                Coord3d(612, -1593, 1893),
                Coord3d(630, 319, -379),
                Coord3d(686, -3108, -505),
                Coord3d(776, -3184, -501),
                Coord3d(846, -3110, -434),
                Coord3d(1135, -1161, 1235),
                Coord3d(1243, -1093, 1063),
                Coord3d(1660, -552, 429),
                Coord3d(1693, -557, 386),
                Coord3d(1735, -437, 1738),
                Coord3d(1749, -1800, 1813),
                Coord3d(1772, -405, 1572),
                Coord3d(1776, -675, 371),
                Coord3d(1779, -442, 1789),
                Coord3d(1780, -1548, 337),
                Coord3d(1786, -1538, 337),
                Coord3d(1847, -1591, 415),
                Coord3d(1889, -1729, 1762),
                Coord3d(1994, -1805, 1792),
            ),
            allBeacons
        )
        assertEquals(79, allBeacons.size)
    }

    @Test
    @Tag("answer")
    fun testAnswer1() {
        val fullInput = findInput(object {}).readLines()
        val scanners = parseScannerReport(fullInput)
        val allBeacons = findAllBeaconsRelativeTo0(scanners)
        assertEquals(367, allBeacons.size)
    }

    @Test
    fun testFindMaxDistance() {
        val scanners = parseScannerReport(sampleReport)
        val maxDistance = findMaxScannerDistance(scanners)
        assertEquals(3621, maxDistance)
    }

    @Test
    @Tag("answer")
    fun testAnswer2() {
        val fullInput = findInput(object {}).readLines()
        val scanners = parseScannerReport(fullInput)
        val maxDistance = findMaxScannerDistance(scanners)
        assertEquals(11925, maxDistance)
    }
}
