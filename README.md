# creek Dolby + Lunaris integration

Curated from the supplied Fix Dolby KSU Lunaris and LunarisDolby packages.

Included:
- 64-bit Dolby DMS HAL and audio libraries
- Dolby DAX3 config and codec XML
- LunarisDolby.apk as priv-app
- Lunaris privapp permissions + hidden API whitelist
- DMS init/context fragments
- Integration snippets and proprietary-files.txt

Intentionally excluded:
- MotoSignatureApp / MotoDolbyDax3 / MotorolaSettingsProvider
- Motorola framework JAR/XML
- daxService
- system_support/
- libsqlite.so
- libstagefright_foundation.so
- 32-bit vendor Dolby libraries
- boot.img
- module scripts

NOTE: audio_effects.xml and the final VINTF manifest must be merged into the creek tree's existing files.
Do not invent UUIDs. SELinux allow rules should be added from actual build/runtime AVC denials.
