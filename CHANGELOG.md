<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# tye-plugin Changelog

## [Unreleased]

### Added

- Tye group to the Services tool window

### Removed

- Tye tool window

### Changed

- Tye global tool version is updated to 0.10.0-alpha.21420.1

### Fixed

- New version check during the startup

## [0.1.3]

### Added

- Action for updating tye global tool
- Option to disable tye global tool new version check

### Changed

- Tye global tool version is updated to 0.9.0-alpha.21380.1

### Fixed
- Fixed ability to connect to Tye Dashboard by [@rohan-buchner](https://github.com/rohan-buchner) ([#51](https://github.com/rafaelldi/tye-plugin/issues/51))
- Fixed stopping process ([#57](https://github.com/rafaelldi/tye-plugin/issues/57))

## [0.1.2]
### Changed
- Compatibility with 2021.2 version of the platform
- Tye global tool version is updated to 0.8.0-alpha.21352.1

## [0.1.1]
### Added
- Check tye version at startup
- Action to update tye global tool
- Action to perform shutdown tye application
- Action to connect to tye host

### Changed
- Plugin icon
- Update tye version to 0.7.0-alpha.21279.2

## [0.1.0]
### Added
- Tool window with tye dashboard
- Dialog to overwrite an existing file during scaffolding 

### Changed
- Tye icons

## [0.0.3]
### Added
- New options in the run configuration to send logs, traces or specify tags.

## [0.0.2]
### Added
- Tye file association
- Tye schema
- Run `tye run` command from the `tye.yaml` file

## [0.0.1]
### Added
- Initial scaffold created from [IntelliJ Platform Plugin Template](https://github.com/JetBrains/intellij-platform-plugin-template)
- Action for install tye global tool
- Action for scaffold `tye.yaml` file into solution folder
- Run configuration for `tye run` command