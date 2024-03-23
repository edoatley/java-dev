# java-dev

Investigating neat ways to do java cloud native dev

## Plan

- Set up dev container supporting docker, java 21/22, postgresql. See [this article](https://medium.com/@alcbotta/from-an-empty-folder-to-a-complete-application-a-walk-through-using-vscode-remote-container-java-39a6fa6e10e2)
- Build an API (Jetty) that calls multiple backends to get a result and saves it to a database - consider [Structured Concurrency](https://docs.oracle.com/en/java/javase/21/core/structured-concurrency.html#GUID-AA992944-AABA-4CBC-8039-DE5E17DE86DB
- Add basic tests
- Add integration test
- Add CI
- Add basic IaC build (registry)
- Add repository push
- Add proper pipeline for `feature` branches 
- Add full dev IaC
