# Goal

To run tests inside the Babashka runtime on file changes.

# Partial success

* Running `bb test:bb` correctly feeds command line args into
  `cognitect.test-runner.api/test`, running tests specified.

* Running `test:bb:watch` correctly starts a file watcher on paths specified in arguments, which then starts `test:bb` with no arguments on file change.

# Failure

After starting the watcher, only the first time saving a file will correctly run tests.

Each time following that: Passing tests will continue to pass after file changes should cause failure. New tests are not picked up. Failing tests may cause the script to hang. If it doesn't hang, then changing a failing test to passing is the only case that correctly updates.

# Reproduce

* Run `bb test:bb` to verify bb is running test suite correctly (starts with one passing test).

* Run `bb test:bb:watch` to start watcher.

* Save `test/skylize/example_test.cljc` to trigger first watcher test of `foo-test`, which again passes.

* Uncomment `bar-test`  and save. See that a test run is triggered, but still only shows one test. The new test is ignored.

* Restart the watcher, and save, to see both tests passing.

* Change `bar-test` to fail (set one of the `true`s to `false`) and save. See that a test run is triggered, but fails to acknowledge the failing test as failing.

* Restart the watcher, and save, to see 1 passing and 1 failed test.

* For the repro, on my system the watcher hangs at this point after the
  failed test. It becomes unresponsive to any further changes being written to file.

* If it did not hang, you can try one more case: Update the failed test to pass, and save. This should actually be picked up correctly, showing 2 passing tests.