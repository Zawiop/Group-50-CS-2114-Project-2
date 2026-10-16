# Group-50-CS-2114-Project-2

Roommate Chore Planner — a console app that stores roommates and chores in
the supplied generic `LinkedChain`, shows a weekly schedule, and rotates
assignments each week.

All source and test files are in `src/` (default package). Tests extend
`student.TestCase` and need `student.jar` on the classpath.

| Class | Owner | Status |
|---|---|---|
| `LinkedChain<T>` | Supplied (integration: Arrush) | Used unchanged |
| `Roommate` | Arrush | Done, tested |
| `Chore` | Arrush | Done, tested |
| `ChorePlanner` | Aryaman | Done, tested |
| `ChorePlannerApp` | Shelby | Done, not yet tested |

## Reflection

### What changed from the specification?

**Storage and model classes (Arrush):**
- `Roommate(int, String)` and `Chore(String, String)` match the spec's
  signatures, fields (`private final`, no setters) and trimming rules.
- The spec asked for "a specific ID message" but didn't give the wording.
  We used `"Roommate ID must be positive."` and
  `"Chore description cannot be null."`. The blank-name messages are exactly
  as written in section 6.
- **Added:** `toString()` on `Roommate` (`"Sam (ID 10)"`) and `Chore`
  (`"Trash - Tie bag"`, or just `"Trash"` when there is no description).
  The spec didn't list these. They make `LinkedChain.toString()` and
  debugging output readable, and they let listings tell apart roommates with
  the same name. They don't change equality.
- As the spec says, neither model overrides `equals`. Tests confirm that a
  chain compares them by reference and that removing by position
  (`get(i)` then `remove(stored)`) removes the exact object, not a different
  roommate with the same name.
- Validation lives where section 6 put it: the model constructors reject bad
  IDs, names and descriptions, and the chain rejects `null` and bad indices.
- **Test cases:** every row of the section 7 storage/model test plan has a
  normal case and a bad-input/boundary case. We added two runtime checks:
  one chain class reused for both `Roommate` and `Chore`, and reference
  equality. The compile-time generic check
  (`LinkedChain<Roommate>.add(new Chore(...))`) was checked by hand. It
  can't live in a test file that compiles, so it isn't in the suite.

**Planner (ChorePlanner):**
- Every public method and signature matches section 5. No public methods
  were added or dropped, and `ChorePlanner` holds the two chains as private
  fields (it does not extend `LinkedChain`).
- The spec fixed some messages but not all. Fixed by the spec and used
  as written: `"Roommate ID already exists."`, `"Week limit reached."`, and
  the two schedule messages (`"Week W: No chores to assign."` and
  `"Week W: Add at least one roommate before generating a schedule."`).
  We chose the wording for the `advanceWeek` empty-state exception
  (`"Add at least one roommate and one chore before advancing the week."`)
  and the schedule layout: a `Week W schedule:` header, then one line per
  chore such as `1. Trash (Tie bag): Aniket (ID 10)`. The description is
  left out when it is empty.
- Validation lives where section 6 put it. The model constructors check
  field values, `addRoommate` checks for duplicate IDs (by scanning
  `getRoommates()`, not `contains`), and `LinkedChain.get` checks removal
  positions. `ChorePlanner` does not repeat the checks the model and the
  chain already do.
- The planner does not print the "Lists changed; current-week assignments
  have been regenerated." message. The spec puts that in the console, so it
  will be added in `ChorePlannerApp`.
- `ChorePlannerTest` follows the section 8 planner table: a normal case and
  a bad-input/boundary case for every public method, plus the integration
  scenario (weeks 1-4, and the week 2 removal and addition). The max-week
  branch has no public setter, so its test sets `weekNumber` with
  reflection (the "test-only fixture" idea from section 8).

_Console changes: TBD by the `ChorePlannerApp` owner._

### What changed in the LinkedChain?

Nothing. `LinkedChain.java` is the supplied file, unchanged. There is no
`removeAt`, so the planner removes by position with `get(index)` followed by
`remove(theStoredObject)`, as planned in section 4. Typed snapshots use
`toArray(new Roommate[size()])` / `toArray(new Chore[size()])`, so no casts
are needed.

### What happened to our unknowns?

- **Removing one of two roommates who share a name** — solved. Without an
  `equals` override, `remove(T)` matches by reference, so the get-then-remove
  pattern removes exactly the selected entry (`LinkedChainTest.testRemoveByPositionPattern`).
- **Getting a typed array out of a generic chain** — solved. The caller passes
  a correctly typed array to `toArray`, so there are no unchecked casts
  (`LinkedChainTest.testReuseWithBothModelTypes`).
- **Chores or roommates added or removed mid-week** — solved. The planner
  stores no schedule. `generateSchedule` recomputes everything from the
  chains and `weekNumber` each time, so an edit keeps the week and shifts
  assignments by the new insertion order (`ChorePlannerTest.testMidWeekEdits`
  checks IDs 30, 10, 30, 10 after removing a roommate in week 2, and
  30, 40, 10, 30 after adding ID 40).
- **How to organize and display the chain data** — solved for the planner.
  It converts each chain to a typed array and builds the text with a
  `StringBuilder`. Still open for the console: _TBD by the
  `ChorePlannerApp` owner._

### What doesn't work yet?

- Storage and model layer: all 47 tests in `LinkedChainTest`, `RoommateTest`
  and `ChoreTest` pass. Nothing is known to be missing from these classes.
- Planner: all 31 tests in `ChorePlannerTest` pass (78 across the four test
  classes). Nothing is known to be missing from `ChorePlanner`.
- The max-week branch of `advanceWeek` is tested only through reflection,
  not through real use.
- Console: `ChorePlannerApp` and its tests do not exist yet, so there is no
  menu and the app cannot be run by a user.
- The section 8 integration scenario has been checked in
  `ChorePlannerTest` but has not been run through the console menu.

## Next Steps

### What is left to finish the MVP?

1. _TBD:_ `ChorePlannerApp`: the menu (choices 0-7), re-prompting on bad
   numbers and unknown choices, 1-based removal positions, clean exit at
   EOF, and the "Lists changed..." message plus new schedule after each
   successful add or remove.
2. _TBD:_ `ChorePlannerAppTest`: scripted `Scanner` input for a valid run,
   a run full of bad input, EOF, and a null `Scanner`.
3. Arrush plus the console owner: run the section 8 integration scenario
   through the menu (3 roommates and 4 chores across weeks 1–4, then
   mid-week edits).

### Which stretch goals will we attempt?

_TBD as a team._ Arrush's view: if a stretch goal needs a new field on a
model (e.g. chore difficulty, or an "away" flag on a roommate), Arrush adds
it to `Roommate`/`Chore` with validation and tests. `LinkedChain` stays
unchanged unless a goal truly needs new storage behavior.

### What new tests will we need?

- Any new model field: a normal and a bad-input test for the constructor and
  each new getter (e.g. difficulty ≤ 0 rejected).
- If we ever override `equals` on a model, rerun
  `LinkedChainTest.testReferenceEquality` and the position-removal test. The
  section 2 equality policy would need updating.
- Console tests (`ChorePlannerAppTest`): TBD, see spec section 8. Each public
  method of `ChorePlannerApp` needs a normal case and a bad-input case.

### Who does what?

| Item | Owner |
|---|---|
| `LinkedChain` integration, `Roommate`, `Chore`, their tests | Arrush Shah |
| Model changes needed by stretch goals | Arrush Shah |
| `ChorePlanner` + `ChorePlannerTest` | Aryaman (done) |
| `ChorePlannerApp` + tests | _TBD_ |
| Integration scenario run | _TBD_ + Arrush Shah |

## Attribution

| File | Who | How |
|---|---|---|
| `src/LinkedChain.java` | CS 2114 Staff; Arrush Shah | Staff wrote it; Arrush added it unchanged and checked it against spec section 4 |
| `src/Roommate.java` | Arrush Shah | Wrote (with GenAI assistance), tested |
| `src/Chore.java` | Arrush Shah | Wrote (with GenAI assistance), tested |
| `src/LinkedChainTest.java` | Arrush Shah | Wrote from Shelby's section 7 test plan (with GenAI assistance) |
| `src/RoommateTest.java` | Arrush Shah | Wrote from Shelby's section 7 test plan (with GenAI assistance) |
| `src/ChoreTest.java` | Arrush Shah | Wrote from Shelby's section 7 test plan (with GenAI assistance) |
| `src/ChorePlanner.java` | Aryaman | Wrote from spec sections 3, 5 and 6 (with GenAI assistance), tested |
| `src/ChorePlannerApp.java` | _TBD_ | _TBD_ |
| `src/ChorePlannerTest.java` | Aryaman | Wrote from the section 8 planner test plan (with GenAI assistance), ran and checked |
| `src/ChorePlannerAppTest.java` | _TBD_ | _TBD_ |

