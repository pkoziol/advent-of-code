package biz.koziolek.adventofcode.year2021.day18

import biz.koziolek.adventofcode.findInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day18Test {

    private val sampleNumbers = """
        [1,2]
        [[1,2],3]
        [9,[8,7]]
        [[1,9],[8,5]]
        [[[[1,2],[3,4]],[[5,6],[7,8]]],9]
        [[[9,[3,8]],[[0,9],6]],[[[3,7],[4,9]],3]]
        [[[[1,3],[5,3]],[[1,3],[8,7]]],[[[4,9],[6,9]],[[8,2],[7,3]]]]
    """.trimIndent().split("\n")

    @Test
    fun testParseAndToString() {
        val numbers = parseSnailfishNumbers(sampleNumbers)
        assertEquals(7, numbers.size)

        assertEquals(
            SnailfishPair(
                SnailfishRegular(1, level = 1),
                SnailfishRegular(2, level = 1),
                level = 0
            ),
            numbers[0]
        )
        assertEquals(sampleNumbers[0], numbers[0].asText())

        assertEquals(
            SnailfishPair(
                SnailfishPair(
                    SnailfishRegular(1, level = 2),
                    SnailfishRegular(2, level = 2),
                    level = 1
                ),
                SnailfishRegular(3, level = 1),
                level = 0
            ),
            numbers[1]
        )
        assertEquals(sampleNumbers[1], numbers[1].asText())

        assertEquals(
            SnailfishPair(
                SnailfishRegular(9, level = 1),
                SnailfishPair(
                    SnailfishRegular(8, level = 2),
                    SnailfishRegular(7, level = 2),
                    level = 1
                ),
                level = 0
            ),
            numbers[2]
        )
        assertEquals(sampleNumbers[2], numbers[2].asText())

        assertEquals(
            SnailfishPair(
                SnailfishPair(
                    SnailfishRegular(1, level = 2),
                    SnailfishRegular(9, level = 2),
                    level = 1
                ),
                SnailfishPair(
                    SnailfishRegular(8, level = 2),
                    SnailfishRegular(5, level = 2),
                    level = 1
                ),
                level = 0
            ),
            numbers[3]
        )
        assertEquals(sampleNumbers[3], numbers[3].asText())

        assertEquals(
            SnailfishPair(
                SnailfishPair(
                    SnailfishPair(
                        SnailfishPair(
                            SnailfishRegular(1, level = 4),
                            SnailfishRegular(2, level = 4),
                            level = 3
                        ),
                        SnailfishPair(
                            SnailfishRegular(3, level = 4),
                            SnailfishRegular(4, level = 4),
                            level = 3
                        ),
                        level = 2
                    ),
                    SnailfishPair(
                        SnailfishPair(
                            SnailfishRegular(5, level = 4),
                            SnailfishRegular(6, level = 4),
                            level = 3
                        ),
                        SnailfishPair(
                            SnailfishRegular(7, level = 4),
                            SnailfishRegular(8, level = 4),
                            level = 3
                        ),
                        level = 2
                    ),
                    level = 1
                ),
                SnailfishRegular(9, level = 1),
                level = 0
            ),
            numbers[4]
        )
        assertEquals(sampleNumbers[4], numbers[4].asText())

        assertEquals(
            SnailfishPair(
                SnailfishPair(
                    SnailfishPair(
                        SnailfishRegular(9, level = 3),
                        SnailfishPair(
                            SnailfishRegular(3, level = 4),
                            SnailfishRegular(8, level = 4),
                            level = 3
                        ),
                        level = 2
                    ),
                    SnailfishPair(
                        SnailfishPair(
                            SnailfishRegular(0, level = 4),
                            SnailfishRegular(9, level = 4),
                            level = 3
                        ),
                        SnailfishRegular(6, level = 3),
                        level = 2
                    ),
                    level = 1
                ),
                SnailfishPair(
                    SnailfishPair(
                        SnailfishPair(
                            SnailfishRegular(3, level = 4),
                            SnailfishRegular(7, level = 4),
                            level = 3
                        ),
                        SnailfishPair(
                            SnailfishRegular(4, level = 4),
                            SnailfishRegular(9, level = 4),
                            level = 3
                        ),
                        level = 2
                    ),
                    SnailfishRegular(3, level = 2),
                    level = 1
                ),
                level = 0
            ),
            numbers[5]
        )
        assertEquals(sampleNumbers[5], numbers[5].asText())

        assertEquals(sampleNumbers[6], numbers[6].asText())
    }

    @Test
    fun testSimpleAddition() {
        val a = parseSnailfishNumber("[1,2]")
        val b = parseSnailfishNumber("[[3,4],5]")
        val expectedSum = parseSnailfishNumber("[[1,2],[[3,4],5]]")

        assertEquals(expectedSum, a + b)
    }

    @Test
    fun testStillSimpleAddition() {
        val a = parseSnailfishNumber("[1,1]")
        val b = parseSnailfishNumber("[2,2]")
        val c = parseSnailfishNumber("[3,3]")
        val d = parseSnailfishNumber("[4,4]")
        val expectedSum = parseSnailfishNumber("[[[[1,1],[2,2]],[3,3]],[4,4]]")

        assertEquals(expectedSum, a + b + c + d)
    }

    @Test
    fun testExplode() {
        assertEquals(
            parseSnailfishNumber("[[[[0,9],2],3],4]"),
            parseSnailfishNumber("[[[[[9,8],1],2],3],4]").explode(),
        )
        assertEquals(
            parseSnailfishNumber("[7,[6,[5,[7,0]]]]"),
            parseSnailfishNumber("[7,[6,[5,[4,[3,2]]]]]").explode()
        )
        assertEquals(
            parseSnailfishNumber("[[6,[5,[7,0]]],3]"),
            parseSnailfishNumber("[[6,[5,[4,[3,2]]]],1]").explode()
        )
        assertEquals(
            parseSnailfishNumber("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]"),
            parseSnailfishNumber("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]").explode()
        )
        assertEquals(
            parseSnailfishNumber("[[3,[2,[8,0]]],[9,[5,[7,0]]]]"),
            parseSnailfishNumber("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]").explode()
        )
    }

    @Test
    fun testSplit() {
        assertEquals(SnailfishRegular(9, level = 0), SnailfishRegular(9, level = 0).split())
        assertEquals(parseSnailfishNumber("[5,5]"), SnailfishRegular(10, level = 0).split())
        assertEquals(parseSnailfishNumber("[5,6]"), SnailfishRegular(11, level = 0).split())
        assertEquals(parseSnailfishNumber("[6,6]"), SnailfishRegular(12, level = 0).split())

        assertEquals(
            SnailfishPair(
                SnailfishRegular(5, level = 3),
                SnailfishRegular(6, level = 3),
                level = 2
            ),
            SnailfishRegular(11, level = 2).split()
        )
    }

    @Test
    fun testReduceStepByStep() {
        val a = parseSnailfishNumber("[[[[4,3],4],4],[7,[[8,4],9]]]")
        val b = parseSnailfishNumber("[1,1]")

        val afterAddition = a + b
        assertEquals(parseSnailfishNumber("[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]"), afterAddition)

        val afterExplode1 = afterAddition.explode()
        assertEquals(parseSnailfishNumber("[[[[0,7],4],[7,[[8,4],9]]],[1,1]]"), afterExplode1)
        assertEquals(afterExplode1, afterAddition.reduceOnce())

        val afterExplode2 = afterExplode1.explode()
        assertEquals(parseSnailfishNumber("[[[[0,7],4],[15,[0,13]]],[1,1]]"), afterExplode2)
        assertEquals(afterExplode2, afterExplode1.reduceOnce())

        val afterSplit1 = afterExplode2.split()
        assertEquals(parseSnailfishNumber("[[[[0,7],4],[[7,8],[0,13]]],[1,1]]"), afterSplit1)
        assertEquals(afterSplit1, afterExplode2.reduceOnce())

        val afterSplit2 = afterSplit1.split()
        assertEquals(parseSnailfishNumber("[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]"), afterSplit2)
        assertEquals(afterSplit2, afterSplit1.reduceOnce())

        val afterExplode3 = afterSplit2.explode()
        assertEquals(parseSnailfishNumber("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]"), afterExplode3)
        assertEquals(afterExplode3, afterSplit2.reduceOnce())
    }

    @Test
    fun testReduce() {
        assertEquals(
            parseSnailfishNumber("[[[[1,1],[2,2]],[3,3]],[4,4]]"),
            """
                [1,1]
                [2,2]
                [3,3]
                [4,4]
            """.trimIndent()
                .split("\n")
                .let { parseSnailfishNumbers(it) }
                .let { addAndReduceAll(it) }
        )

        assertEquals(
            parseSnailfishNumber("[[[[3,0],[5,3]],[4,4]],[5,5]]"),
            """
                [1,1]
                [2,2]
                [3,3]
                [4,4]
                [5,5]
            """.trimIndent()
                .split("\n")
                .let { parseSnailfishNumbers(it) }
                .let { addAndReduceAll(it) }
        )

        assertEquals(
            parseSnailfishNumber("[[[[5,0],[7,4]],[5,5]],[6,6]]"),
            """
                [1,1]
                [2,2]
                [3,3]
                [4,4]
                [5,5]
                [6,6]
            """.trimIndent()
                .split("\n")
                .let { parseSnailfishNumbers(it) }
                .let { addAndReduceAll(it) }
        )

        assertEquals(
            parseSnailfishNumber("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]"),
            """
                [[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]
                [7,[[[3,7],[4,3]],[[6,3],[8,8]]]]
                [[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]
                [[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]
                [7,[5,[[3,8],[1,4]]]]
                [[2,[2,2]],[8,[8,1]]]
                [2,9]
                [1,[[[9,3],9],[[9,0],[0,7]]]]
                [[[5,[7,4]],7],1]
                [[[[4,2],2],6],[8,7]]
            """.trimIndent()
                .split("\n")
                .let { parseSnailfishNumbers(it) }
                .let { addAndReduceAll(it) }
        )

        assertEquals(
            parseSnailfishNumber("[[[[6,6],[7,6]],[[7,7],[7,0]]],[[[7,7],[7,7]],[[7,8],[9,9]]]]"),
            """
                [[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]
                [[[5,[2,8]],4],[5,[[9,9],0]]]
                [6,[[[6,2],[5,6]],[[7,6],[4,7]]]]
                [[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]
                [[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]
                [[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]
                [[[[5,4],[7,7]],8],[[8,3],8]]
                [[9,3],[[9,9],[6,[4,9]]]]
                [[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]
                [[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]
            """.trimIndent()
                .split("\n")
                .let { parseSnailfishNumbers(it) }
                .let { addAndReduceAll(it) }
        )
    }

    @Test
    fun testMagnitude() {
        assertEquals(29, parseSnailfishNumber("[9,1]").magnitude)
        assertEquals(21, parseSnailfishNumber("[1,9]").magnitude)
        assertEquals(129, parseSnailfishNumber("[[9,1],[1,9]]").magnitude)
        assertEquals(143, parseSnailfishNumber("[[1,2],[[3,4],5]]").magnitude)
        assertEquals(1384, parseSnailfishNumber("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]").magnitude)
        assertEquals(445, parseSnailfishNumber("[[[[1,1],[2,2]],[3,3]],[4,4]]").magnitude)
        assertEquals(791, parseSnailfishNumber("[[[[3,0],[5,3]],[4,4]],[5,5]]").magnitude)
        assertEquals(1137, parseSnailfishNumber("[[[[5,0],[7,4]],[5,5]],[6,6]]").magnitude)
        assertEquals(3488, parseSnailfishNumber("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]").magnitude)
        assertEquals(4140, parseSnailfishNumber("[[[[6,6],[7,6]],[[7,7],[7,0]]],[[[7,7],[7,7]],[[7,8],[9,9]]]]").magnitude)
    }

    @Test
    fun testAnswer1() {
        val fullInput = findInput(object {}).readLines()
        val numbers = parseSnailfishNumbers(fullInput)
        val sum = addAndReduceAll(numbers)
        assertEquals(4176, sum.magnitude)
    }
}
