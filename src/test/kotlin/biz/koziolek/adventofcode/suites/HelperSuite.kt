package biz.koziolek.adventofcode.suites

import org.junit.platform.suite.api.ExcludeTags
import org.junit.platform.suite.api.SelectPackages
import org.junit.platform.suite.api.Suite

@Suite
@SelectPackages("biz.koziolek.adventofcode")
@ExcludeTags(
    "2021",
    "2022",
    "2023",
)
class HelperSuite
