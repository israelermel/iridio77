# Iridio77

![Build](https://github.com/israelermel/iridio77/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/PLUGIN_ID.svg)](https://img.shields.io/jetbrains/plugin/v/br.com.vineivel.Iridio77)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/PLUGIN_ID.svg)](https://img.shields.io/jetbrains/plugin/d/br.com.vineivel.Iridio77)

## Description

<!-- Plugin description -->
`Iridio77` is a plugin that can help developers to be more productive by 
providing additional functionality related to ADB commands. 
The plugin allows developers to manipulate properties on the file `local.properties`,
and offers other options to automate some tasks and save time

### Accessibility
- Enable/Disable TalkBack
- Change Display daltonizer
- Enable/Disable Color Inversion

### Developer Options
- Change Density
- Change Font Size
- Enable/Disable Layout Bounds
- Enable/Disable Profile
- Enable/Disable Overdraw
- Enable/Disable Animations
- Enable/Disable Screen Touches
- Remove packages(Apps)
- Reset to default configurations

### Settings
- Add custom attributes in local.properties via <kbd>Settings</kbd> > <kbd>Iridio Settings</kbd>

<!-- Plugin description end -->

## Installation

- Using IDE built-in plugin system:

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "Iridio77"</kbd> >
  <kbd>Install Plugin</kbd>

- Manually:

  To Run locally, uncomment `localPath.set(properties("studioCompilePath"))` on  [Gradle](/build.gradle.kts) and
  comment `version.set(properties("platformVersion"))`.

- After that go to menu <kbd>Select Run/Debug Configuration</kbd> > <kbd>Run Plugin</kbd> to install the Iridio77
  plugin on your Android Studio

---
