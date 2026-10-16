# Group-50-CS-2114-Project-2

**Roommate Chore Planner** is a console application that keeps track of
roommates and chores, shows a weekly chore schedule, and rotates assignments
when the user moves on to the next week. Both lists are stored in the
supplied generic `LinkedChain` class.

This README goes with **Deliverable 3, the prototype**: a working slice of
the system plus the tests that show it behaves as specified.

## Project layout

All classes are in the default package under `src/`.

| File | Role | Status |
|---|---|---|
| `LinkedChain.java` | Generic storage, supplied by course staff | Used unchanged |
| `Roommate.java` | Model: positive ID and name | Complete |
| `Chore.java` | Model: name and optional instructions | Complete |
| `ChorePlanner.java` | Holds both chains; builds and rotates the schedule | Complete |
| `ChorePlannerApp.java` | Scanner-based text menu (`main` lives here) | Complete |
| `LinkedChainTest.java` | Storage contract tests | 29 tests, passing |
| `RoommateTest.java` | Roommate tests | 9 tests, passing |
| `ChoreTest.java` | Chore tests | 9 tests, passing |
| `ChorePlannerTest.java` | Planner and integration tests | 31 tests, passing |
| `ChorePlannerAppTest.java` | Scripted console tests | 6 tests, passing |

## Building, running and testing

The tests extend `student.TestCase`, so `student.jar` from the CS 2114
support files must be on the classpath. In Eclipse, add `student.jar` to the
build path, run `ChorePlannerApp` as a Java application, and run the test
classes as JUnit tests. From a terminal:

```bash
mkdir -p bin && javac -d bin -cp path/to/student.jar src/*.java
```

```bash
java -cp bin ChorePlannerApp
```

```bash
java -cp bin:path/to/student.jar org.junit.runner.JUnitCore LinkedChainTest RoommateTest ChoreTest ChorePlannerTest ChorePlannerAppTest
```

Current result: **84 tests, 0 failures.**

### Menu

| Choice | Action |
|---|---|
| 1 | Add roommate (ID, then name) |
| 2 | Remove roommate by its displayed position |
| 3 | Add chore (name, then an optional description) |
| 4 | Remove chore by its displayed position |
| 5 | List roommates and chores |
| 6 | Show the current week's schedule |
| 7 | Advance to the next week and show the new schedule |
| 0 | Exit (end of input also exits) |

Example: add roommates 10 Aniket, 20 Sam and 30 Joe, then the chores Trash,
Dishes, Bathroom and Vacuum. Week 1 assigns IDs 10, 20, 30, 10. After
choosing 7, week 2 assigns 20, 30, 10, 20. Removing Sam during week 2 keeps
the week at 2 and gives 30, 10, 30, 10.

## How the application uses LinkedChain

- **Reuse.** `ChorePlanner` holds a private `LinkedChain<Roommate>` and a
  private `LinkedChain<Chore>`. Both are the same generic class; there are
  no copied, type-specific versions. `ChorePlanner` does not extend
  `LinkedChain`.
- **Public API only.** The planner uses only the chain's public methods:
  `addToEnd`, `get`, `remove(T)`, `size`, `isEmpty` and `toArray`. All the
  rules about IDs, scheduling and weeks live in `ChorePlanner`, and the menu
  logic lives in `ChorePlannerApp`. `LinkedChain` has no application logic.
- **Insertion order.** Entries are added with `addToEnd`, so the chain order
  is the order shown on screen.
- **Typed snapshots.** `getRoommates()` and `getChores()` call `toArray`
  with a correctly typed array, e.g. `toArray(new Roommate[size()])`, so the
  code needs no casts or raw types. Changing a returned array does not change
  the chain.
- **Equality.** `Roommate` and `Chore` do not override `equals`, so the
  chain compares them as exact objects (by reference). To remove an entry by
  position, the planner calls `get(index)` and passes the result to
  `remove(T)`. That removes the selected entry even when two roommates share
  a name.
- **Duplicate IDs.** `addRoommate` scans the `getRoommates()` snapshot for a
  matching `getId()`. It does not use `contains`, since `contains` compares
  objects by reference, not by ID.

## Bad input handled in this prototype

| Case | Where it is caught | Response |
|---|---|---|
| Non-numeric, blank or whitespace-only menu choice | `ChorePlannerApp.run` | "Invalid input. Please enter a number." Shows the menu again |
| Unknown menu number (e.g. 9 or 99) | `ChorePlannerApp.run` | "Unknown choice. Please choose 0-7." |
| Non-numeric or too-large ID or position (e.g. `99999999999`) | `ChorePlannerApp.readInteger` | Asks again for the same value |
| Roommate ID ≤ 0 | `Roommate` constructor | "Roommate ID must be positive." Back to the menu |
| Duplicate roommate ID | `ChorePlanner.addRoommate` | "Roommate ID already exists." Back to the menu |
| Null or blank roommate name | `Roommate` constructor | "Roommate name cannot be blank." |
| Null or blank chore name | `Chore` constructor | "Chore name cannot be blank." |
| Null chore description | `Chore` constructor | "Chore description cannot be null." (An empty description is allowed.) |
| Removal position outside 1..size | `ChorePlannerApp` (console check); `LinkedChain.get` if the planner is called directly | "Invalid roommate/chore position." / `IndexOutOfBoundsException` |
| Removing from an empty list | `ChorePlannerApp` | "There are no roommates/chores to remove." |
| Schedule with no chores or no roommates | `ChorePlanner.generateSchedule` | A guidance message; no division by zero |
| Advancing with an empty list | `ChorePlanner.advanceWeek` | `IllegalStateException`, message printed; week unchanged |
| Advancing past `Integer.MAX_VALUE` | `ChorePlanner.advanceWeek` | "Week limit reached."; no overflow |
| End of input at any prompt | `ChorePlannerApp.readLine` | Exits cleanly; a partly entered add is not sent to the planner |
| Null `Scanner` | `ChorePlannerApp` constructor | `IllegalArgumentException("Scanner cannot be null.")` |

Every case above was run through the real menu or a unit test, and none of
them crashes the program. A rejected action leaves the lists and the week
number unchanged.

## Reflection

### What changed from our specification?

**Classes.** The five-class design from the specification is what we built:
`LinkedChain`, `Roommate`, `Chore`, `ChorePlanner` and `ChorePlannerApp`. No
classes were added or merged.

**Method signatures.**
- Every public constructor and method in section 5 exists with the
  signature we specified. No public methods were dropped.
- We added `toString()` to both models (`"Sam (ID 10)"`, and
  `"Trash - Tie bag"` or just `"Trash"`). The console prints these in
  listings, and the ID tells apart roommates with the same name. They don't
  change equality.
- `ChorePlannerApp` has private helpers (`readLine`, `readInteger`, one
  method per menu action, and `listsChanged`), which section 9 allowed.
- In `ChorePlannerApp`, the `input` and `planner` fields are not `final`,
  although section 3 said they would be. This doesn't change behavior; it
  is on our fix list.

**Messages and output.**
- These are used exactly as specified: "Roommate name cannot be blank.",
  "Chore name cannot be blank.", "Roommate ID already exists.", "Week limit
  reached.", "Lists changed; current-week assignments have been
  regenerated.", and both empty-schedule messages.
- The specification asked for a specific message for a bad ID or a null
  description without giving the wording. We chose "Roommate ID must be
  positive." and "Chore description cannot be null.".
- For `advanceWeek` on an empty list we chose "Add at least one roommate
  and one chore before advancing the week.".
- The schedule has a `Week W schedule:` header, then one line per chore,
  e.g. `1. Trash (Tie bag): Aniket (ID 10)`.
- After advancing, the console also prints "Advanced to week W." before the
  new schedule.
- Sections 6 and 9 specified "Position is outside the list." and "There are
  no entries to remove.". The console prints more specific messages instead
  ("Invalid roommate position.", "There are no chores to remove.", and so
  on). The behavior is the same; only the wording differs.

**Where validation lives.**
- This matches section 6. The model constructors check their own fields.
  `ChorePlanner` checks for duplicate IDs and the conditions for advancing
  the week. The console handles parsing.
- One detail changed: the console checks the 1-based position against the
  list length *before* calling the planner, as section 9 describes, so it
  never needs to catch `IndexOutOfBoundsException`. If the planner is called
  directly with a bad index, `LinkedChain.get` still throws, and the planner
  tests cover that.
- The console catches only `IllegalArgumentException`,
  `IllegalStateException` and `NumberFormatException`, never a general
  `Exception`.

**Test cases.**
- `LinkedChainTest`, `RoommateTest` and `ChoreTest` follow the section 7
  plan row by row. Each public method has at least one normal case and one
  bad-input or boundary case. We added tests that reuse the chain for both
  model types, that check exact-object equality, and that remove by
  position.
- `ChorePlannerTest` follows the section 8 planner table and includes the
  integration scenario: weeks 1–4, then a roommate removed and another added
  during week 2.
- The max-week branch has no public setter, so its test sets `weekNumber`
  with reflection. This is the "test-only fixture" that section 8 allowed.
- The compile-time check that a `LinkedChain<Roommate>` rejects a `Chore`
  was confirmed by hand. A file that intentionally fails to compile can't be
  in the suite.
- `ChorePlannerAppTest` is smaller than the section 8 console plan. It
  covers exit, invalid and unknown menu input, end of input, adding and
  scheduling, removal, and a null `Scanner`. Its tests check that the
  program finishes and reads all of its input; they do not yet check the
  printed text. We confirmed the remaining cases by running the program:
  - huge integers;
  - whitespace-only input;
  - a duplicate ID, a negative ID and a blank name;
  - a position outside the list;
  - end of input partway through an add.

### What changed in the LinkedChain?

Nothing. `LinkedChain.java` is used exactly as supplied. There is no
`removeAt` method, but `get(index)` followed by `remove(storedObject)` covers
removal by position. `addToEnd` keeps insertion order, and `toArray(T[])`
returns typed snapshots. The application needed nothing else from the class.

### What happened to our unknowns?

- **Removing one of two roommates with the same name.** Solved. Since the
  models don't override `equals`, `remove(T)` matches the exact object, so
  removing by position takes out only the selected entry.
- **Getting a typed array out of a generic chain without casts.** Solved,
  by passing a correctly typed array to `toArray`.
- **Chores or roommates added or removed mid-week.** Solved. The planner
  stores no schedule. `generateSchedule` rebuilds it from the chains and the
  week number every time, so an edit keeps the week and assignments follow
  the new list order. After removing Sam in week 2, the assignments are
  30, 10, 30, 10; after adding ID 40, they are 30, 40, 10, 30.
- **Organizing and displaying the chain data.** Solved. The planner turns
  each chain into a typed array and builds the schedule text with a
  `StringBuilder`. The console prints numbered lists of those arrays.
- **Recovering from bad console input.** Solved. Every prompt reads a whole
  line with `hasNextLine()`/`nextLine()` and converts numbers with
  `Integer.valueOf`, so bad text and overflowing numbers are rejected
  without crashing. End of input exits cleanly at any prompt.
- **Testing console output automatically.** Still open. The app tests check
  that each run finishes and reads all its input, but they don't yet compare
  the printed output.

### What doesn't work yet?

- **MVP features.** Every MVP feature from the specification works through
  the menu, and we know of no failing behavior.
- **Gaps from the specification:**
  - `ChorePlannerApp` fields are not `final`;
  - some console messages differ from the wording in sections 6 and 9;
  - the console prints an extra "Advanced to week W." line.
- **Tests still missing:**
  - `ChorePlannerAppTest` does not check printed output;
  - it has no test for `main`;
  - several console bad-input cases from section 8 (huge integer,
    whitespace-only input, removal position 0, end of input in the middle
    of an add, and a valid command after an error) were checked by hand but
    have no automated test yet.
- **Failing tests:** none. All 84 tests pass.

## Next Steps

### What is left to finish the MVP?

In the order we will do them:

1. **Strengthen `ChorePlannerAppTest`.** Use `systemOut().getHistory()` to
   check the printed output for every test. Add tests for the section 8
   console cases that are only checked by hand today, plus a test for
   `main`.
2. **Match the specification.** Make the `ChorePlannerApp` fields `final`.
   Decide as a group whether to keep the more specific console messages
   (and update the specification) or switch to the specified wording.
3. **Run the full integration again** (section 8) through the menu, then
   with one roommate and several chores, and with four roommates and one
   chore.
4. **Polish.** Make the output consistent (show the week number with list
   output) and check that every Javadoc comment matches the code.

### Which stretch goals will we attempt?

- **Attempting: completion tracking.** Mark a chore done for the current
  week, show it in the schedule, and clear it when the week advances. This
  fits the current design: the planner keeps the record, and
  `LinkedChain` stays unchanged.
- **Attempting: chore swaps.** Two roommates trade one chore for the
  current week only. The planner keeps the swap and applies it when it
  builds the schedule.
- **Dropping: away mode, difficulty-based balancing and saved schedule
  history.** Each needs a different scheduling rule or stored history that
  doesn't fit our "rebuild the schedule from the chains" design. We will
  only revisit them if the MVP and both goals above are finished early.

### What new tests will we need?

- **Completion tracking:** marking a valid chore done (normal); a position
  outside the list, marking the same chore twice, and marking with no
  schedule (bad input); marks clearing after `advanceWeek`; marks staying
  correct after a mid-week edit.
- **Chore swaps:** a valid swap between two roommates (normal); swapping a
  roommate with themselves, using an invalid position, or swapping when only
  one roommate exists (bad input); the swap disappearing after
  `advanceWeek`.
- **Console:** new menu options with valid input, non-numeric input,
  out-of-range positions, and end of input partway through.
- **Regression:** rerun all five test classes after every change.

### Who does what?

Each item has one owner, and the rest of the group reviews it before it is
merged.

| Item | Owner |
|---|---|
| Strengthen `ChorePlannerAppTest` (output checks, `main`, hand-checked cases) | Shelby Sears |
| `ChorePlannerApp` fixes to match the specification; console menu options for stretch goals | Shelby Sears |
| Completion tracking in `ChorePlanner` and its tests | Aniket Nair-Das |
| Chore swaps in `ChorePlanner` and its tests | Aryaman Kapoor |
| Model or storage changes needed by stretch goals; integration runs | Arrush Shah |
| Final README and review of the whole submission | Whole group |

## Attribution

| File | Who | How |
|---|---|---|
| `src/LinkedChain.java` | CS 2114 staff; Arrush Shah | Staff wrote it. Arrush added it unchanged and checked it against spec section 4. Group reviewed. |
| `src/Roommate.java` | Arrush Shah | Wrote with GenAI assistance and tested. Group reviewed. |
| `src/Chore.java` | Arrush Shah | Wrote with GenAI assistance and tested. Group reviewed. |
| `src/ChorePlanner.java` | Aryaman Kapoor | Wrote from spec sections 3, 5 and 6 with GenAI assistance, and tested. Group reviewed. |
| `src/ChorePlannerApp.java` | Shelby Sears | Wrote with GenAI assistance and tested. Group reviewed. |
| `src/LinkedChainTest.java` | Aniket Nair-Das | Wrote from the section 7 test plan with GenAI assistance. Group reviewed. |
| `src/RoommateTest.java` | Aniket Nair-Das | Wrote from the section 7 test plan with GenAI assistance. Group reviewed. |
| `src/ChoreTest.java` | Arrush Shah | Wrote from the section 7 test plan with GenAI assistance. Group reviewed. |
| `src/ChorePlannerTest.java` | Aryaman Kapoor | Wrote from the section 8 planner test plan with GenAI assistance, then ran and checked it. Group reviewed. |
| `src/ChorePlannerAppTest.java` | Shelby Sears | Wrote with GenAI assistance and tested. Group reviewed. |
| `README.md` | Whole group | Written and reviewed together, with GenAI assistance. |
