# Changelog

## Unreleased

### Fixed

- Drag and drop now accepts one regular file at a time and explains why multiple files or folders are rejected.
- Duplicate checks now treat the default port, domain letter case, final domain dot, and equivalent IPv6 forms correctly.
- Import results now show inserted entries, skipped duplicates, and the backup file in one clear status message.
- OpenNBT is now pinned to the exact source revision used by the project.

## v1.0.0

First stable release.

### Added

- Maven Wrapper, so the project builds without a local Maven install.
- GitHub Actions build workflow.
- Automated tests for server list parsing and `servers.dat` writing.
- Versioned release jar name.

### Improved

- UI actions for loading preview and inserting servers now run in background workers.
- Added a status bar for load/import feedback.
- Added file chooser filters for `.txt` and `.dat`.
- Added a button for the default Minecraft `servers.dat` path.
- Moved code from `org.example` to `dev.hexwald.serverinserter`.
