# Take-Home Challenge Thermondo

## Setup Instructions
* Install AS Quail 3 (or any AS version that supports AGP 9.3.1)
  * AGP 9.1.0+ was required since my Phone uses Sdk 37
  * Decrease AGP version & SDK versions, if your AS does not support AGP 9.1.0+ and SDK 37 not required

## Architecture Decisions
* I chose Koin as the DI Framework
* I chose MockK as the Mocking Framework
* I chose Coroutines & Flows over RxJava for reactive programming
* I chose MVI over MVVM because I felt a Temperature Conflict was more of an expected occurrence and not really an Exception

## Trade-Offs
* I went with a hacky approach to ensure a Conflict would arise between User changes & background job changes

## Open TODOs
* Because of time-constraints I only left TODOs for the "collaborativeMode" Toggle and to show the Snackbar
  * But the implementation allows to simply switch "val collaborativeMode = ON" to take the path that auto-resolves the Conflict by taking the technicians value and that would show the Snackbar

## AI Usage
* I used Claude Code within the AS terminal
* I used AI to add library dependencies for Koin, Coroutines and MockK
* I used AI to generate the Testcases and adjusted them by hand afterward
* I used AI to generate the AlertDialog according to "// TODOs" I left as instructions in ResolveConflictDialog and adjusted the logic by hand afterward
* I used AI to generate the background job in THCThermondoApplication