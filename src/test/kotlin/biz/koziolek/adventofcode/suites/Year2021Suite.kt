package biz.koziolek.adventofcode.suites

import org.junit.platform.suite.api.IncludeTags
import org.junit.platform.suite.api.SelectPackages
import org.junit.platform.suite.api.Suite

@Suite
@SelectPackages("biz.koziolek.adventofcode")
@IncludeTags("2021")
internal class Year2021Suite
