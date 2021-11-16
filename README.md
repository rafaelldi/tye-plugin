# tye-plugin

![Build](https://github.com/rafaelldi/tye-plugin/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/16426-tye.svg)](https://plugins.jetbrains.com/plugin/16426-tye)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/16426-tye.svg)](https://plugins.jetbrains.com/plugin/16426-tye)

<!-- Plugin description -->
Adds support for [Project Tye](https://github.com/dotnet/tye) tool to develop distributed applications. An unofficial plugin.

Tye simplifies microservices development with local orchestrating of multiple .NET projects and docker containers. 

**Features:**
* Installation a `microsoft.tye` global tool - Install the tool quickly from the menu.
* Scaffolding a tye configuration file - Quick start with the default configuration file for a solution.
* Executing `tye run` command - Run all your services from the solution.
* Tye configuration file schema support.
* Tye dashboard - See a summary of the running services and containers.
<!-- Plugin description end -->

## Tye Installation

1. Install [.NET 6](https://dotnet.microsoft.com/download/dotnet/6.0).
2. Run action from the menu <kbd>Tools</kbd> -> <kbd>Tye</kbd> -> <kbd>Install Tye Global Tool</kbd> OR install tye via the [following command](https://github.com/dotnet/tye/blob/main/docs/getting_started.md):
```shell
dotnet tool install -g Microsoft.Tye --version "0.10.0-alpha.21420.1"
```

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
