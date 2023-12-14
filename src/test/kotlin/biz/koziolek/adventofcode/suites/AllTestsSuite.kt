package biz.koziolek.adventofcode.suites

import org.junit.platform.suite.api.SelectClasses
import org.junit.platform.suite.api.Suite

@Suite
@SelectClasses(
    HelperSuite::class,
    Year2021Suite::class,
    Year2022Suite::class,
    Year2023Suite::class,
)
class AllTestsSuite
