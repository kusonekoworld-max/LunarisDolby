# LunarisDolby — creek integration

This repository contains only the LunarisDolby app integration.

It intentionally does NOT carry the Dolby DMS HAL, Dolby vendor libraries,
Motorola framework/apps, KSU module scripts, broad module SELinux policy,
manifest patching, or resetprop scripts.

The target creek tree is expected to already provide the Dolby DMS 2.0
service, vendor libraries, VINTF manifest, and audio-effect configuration.

## Integration

Copy the repository contents into the corresponding AOSP/Axion tree, or
use the provided fragment as a guide.

Required:
- Android.bp at the source root must be visible to Soong.
- Add `LunarisDolby` to PRODUCT_PACKAGES.
- Copy the two XML files to system_ext/etc.