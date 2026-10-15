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
| `ChorePlanner` | _TBD_ | _TBD_ |
| `ChorePlannerApp` | _TBD_ | _TBD_ |

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

_Planner/console changes: TBD by owners._

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
- _Remaining unknowns from the scope: TBD by planner/console owners._

### What doesn't work yet?

- Storage and model layer: all 47 tests in `LinkedChainTest`, `RoommateTest`
  and `ChoreTest` pass. Nothing is known to be missing from these classes.
- _Planner and console status: TBD._

## Next Steps

### What is left to finish the MVP?

1. _TBD:_ `ChorePlanner` (add/remove roommates and chores, `generateSchedule`, `advanceWeek`).
2. _TBD:_ `ChorePlannerApp` menu and input handling.
3. Arrush: once the planner exists, help run the section 8 integration
   scenario (3 roommates and 4 chores across weeks 1–4, then mid-week edits).

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
- _Planner/console tests: TBD (see spec section 8)._

### Who does what?

| Item | Owner |
|---|---|
| `LinkedChain` integration, `Roommate`, `Chore`, their tests | Arrush Shah |
| Model changes needed by stretch goals | Arrush Shah |
| `ChorePlanner` + tests | _TBD_ |
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
| `src/ChorePlanner.java` | _TBD_ | _TBD_ |
| `src/ChorePlannerApp.java` | _TBD_ | _TBD_ |
| Planner/console test files | _TBD_ | _TBD_ |
