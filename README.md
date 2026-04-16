<div align="center">

<img src="https://github.com/user-attachments/assets/f829406e-b3d0-4125-8373-50b66ba81eec" width="100" height="100" alt="LOGO" />

# Colored

Colored is a simple plugin that allows your players to format their chat using classic color codes.

[![Colored](https://img.shields.io/hangar/dt/Colored?link=https%3A%2F%2Fhangar.papermc.io%2FQuickMash%2FColored&style=flat)](https://hangar.papermc.io/QuickMash/Colored)
![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/QuickMash/Colored/build-and-publish.yml)
![GitHub Issues or Pull Requests](https://img.shields.io/github/issues/QuickMash/Colored)

<img src="https://github.com/user-attachments/assets/8ac2da89-018b-47f8-85b3-abf9d4c6f512" width="80%" alt="Screenshot" />

</div>

---

## 💡 What is *Colored*?

Colored is a simple plugin that allows your players to format their chat, the same way as you would format your server's MOTD. Simply type color codes into the chat, and your message will be automatically formatted.

## 🎨 Color Chart

| Code | Color | Code | Color | Code | Color | Code | Color |
| :---: | :--- | :---: | :--- | :---: | :--- | :---: | :--- |
| **&0** | Black | **&5** | Dark Purple | **&a** | Green | **&f** | White |
| **&1** | Dark Blue | **&6** | Gold | **&b** | Aqua | **&r** | Reset |
| **&2** | Dark Green | **&7** | Light Gray | **&c** | Red | | |
| **&3** | Dark Aqua | **&8** | Gray | **&d** | Purple | | |
| **&4** | Dark Red | **&9** | Blue | **&e** | Yellow | | |

## ✨ Format Codes

| Code | Formatting |
| :---: | :--- |
| **&l** | **Bold** |
| **&m** | ~~Strikethrough~~ |
| **&n** | <u>Underline</u> |
| **&o** | *Italic* |
| **&k** | Obfuscated |

---

## 🤝 Contributing
Don't be afraid to contribute! This is how the plugin gets better. 
* **Fixes:** Submit a PR for any bugs you find.
* **Features:** Create an issue and label it as an `enhancement`.

## 🚀 Release Publishing (GitHub Releases)

The release workflow is in `.github/workflows/build-and-publish.yml` and runs on published releases.

- The plugin version is set from `github.ref_name` (`-PreleaseVersion=...`) so plugin metadata and published versions match the Git tag.
  - Local/CI precedence is `-PreleaseVersion` first, then `GITHUB_REF_NAME` as a fallback.
- Release publishing runs `clean test verifySingleReleaseJar` before upload.
- Build artifact validation uses `verifySingleReleaseJar` and publishing fails unless exactly one release JAR exists in `build/libs` (excluding `-sources`, `-javadoc`, `-dev`, `-plain` jars).
- Required repository secrets:
  - `MODRINTH_TOKEN`
  - `HANGAR_TOKEN`
- Current project identifiers used by the workflow:
  - Modrinth `project_id`: `colored`
  - Hangar `project_id`: `QuickMash/Colored`

Recommended local release check:

```bash
./gradlew clean verifySingleReleaseJar -PreleaseVersion=v1.0.0
```
