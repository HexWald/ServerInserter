# Changelog

## Unreleased

- Write to a temporary file before replacing `servers.dat`. A failed write leaves the original file intact.
- Reject malformed addresses and invalid ports in the preview, with the line number in the error.
- Keep the selected paths fixed while loading or importing.
- Show the import counts and backup name on a separate line.
- Accept comma separators and UTF-8 files with a byte order mark.
- Skip duplicate addresses, including addresses written with the default port.
- Accept one file per drop and clear the preview when the input changes.
- Pin the OpenNBT revision and run the build on Windows and Linux.

## v1.0.0

- Added the Maven Wrapper, a GitHub Actions build and tests for parsing and import.
- Packaged the app as a runnable JAR.
- Moved preview and import work off the UI thread.
- Added import status, file filters and a shortcut to the default Minecraft folder.
