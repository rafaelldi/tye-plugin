# tye-plugin

![Build](https://github.com/rafaelldi/tye-plugin/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)

<!-- Plugin description -->
Adds support for [Project Tye](https://github.com/dotnet/tye) tool to develop distributed applications. Unofficial plugin.
<!-- Plugin description end -->

## Tye Installation

1. Install [.NET Core 3.1](https://dotnet.microsoft.com/download/dotnet/3.1).
2. Install tye via the following command:
```shell
dotnet tool install -g Microsoft.Tye --version "0.6.0-alpha.21070.5"
```
OR run action from the menu <kbd>Tools</kbd> -> <kbd>Tye</kbd> -> <kbd>Install Tye Global Tool</kbd>

## Plugin Installation

- Using IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "tye-plugin"</kbd> >
  <kbd>Install Plugin</kbd>
  
- Manually:

  Download the [latest release](https://github.com/rafaelldi/tye-plugin/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
