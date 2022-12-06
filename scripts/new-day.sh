SCRIPT=$(readlink -f "$0")
SCRIPT_DIR=$(dirname "$SCRIPT")

if [[ $# -ge 1 ]]; then
  YEAR=$1
else
  YEAR=$(date "+%Y")
fi

if [[ $# -ge 2 ]]; then
  DAY=$2
else
  DAY=$(date "+%-d")
fi

DAY=$(echo "$DAY" | sed 's/^0*//')
DAY_LEADING_ZERO=$(printf "%02d" "$DAY")

echo "$YEAR-12-$DAY_LEADING_ZERO"

SRC_DIR="$SCRIPT_DIR/../src/main/kotlin/biz/koziolek/adventofcode/year$YEAR/day$DAY_LEADING_ZERO"
SRC_FILE="$SRC_DIR/day${DAY}.kt"
INPUT_DIR="$SCRIPT_DIR/../src/main/resources/biz/koziolek/adventofcode/year$YEAR/day$DAY_LEADING_ZERO"
INPUT_FILE="$INPUT_DIR/input"
TEST_DIR="$SCRIPT_DIR/../src/test/kotlin/biz/koziolek/adventofcode/year$YEAR/day$DAY_LEADING_ZERO"
TEST_FILE="$TEST_DIR/Day${DAY}Test.kt"

mkdir -p "$SRC_DIR"
mkdir -p "$INPUT_DIR"
mkdir -p "$TEST_DIR"

if [[ -f "$SRC_FILE" ]]; then
  echo "Source already exists."
else
  echo "Creating source..."
  cat "$SCRIPT_DIR/templates/dayX.kt" \
    | sed "s/{YEAR}/$YEAR/g" \
    | sed "s/{DAY}/$DAY/g" \
    | sed "s/{DAY_LEADING_ZERO}/$DAY_LEADING_ZERO/g" \
    > "$SRC_FILE"
fi

if [[ -f "$TEST_FILE" ]]; then
  echo "Test already exists."
else
  echo "Creating test..."
  cat "$SCRIPT_DIR/templates/DayXTest.kt" \
    | sed "s/{YEAR}/$YEAR/g" \
    | sed "s/{DAY}/$DAY/g" \
    | sed "s/{DAY_LEADING_ZERO}/$DAY_LEADING_ZERO/g" \
    > "$TEST_FILE"
fi

if [[ -f "$INPUT_FILE" ]]; then
  echo "Input already exists."
else
  echo "Downloading input..."
  curl --user-agent 'https://github.com/pkoziol/advent-of-code/' \
       --cookie "session=$ADVENT_OF_CODE_SESSION" \
       --output "$INPUT_FILE" \
       "https://adventofcode.com/$YEAR/day/$DAY/input"
fi

if command -v idea &> /dev/null; then
  echo "Opening the files..."
  idea "$SRC_FILE" "$TEST_FILE" "$INPUT_FILE"
fi

if command -v open &> /dev/null; then
  echo "Opening the website..."
  open "https://adventofcode.com/$YEAR/day/$DAY"
fi

echo "🎄 Happy coding! 🎄"
