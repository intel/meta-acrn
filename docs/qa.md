# Testing Procedure

## Test Hardware

All tests should be ran primarily on the supported platform:

* Kaby Lake NUC ([details here](https://projectacrn.github.io/2.4/reference/hardware.html#verified-platforms-according-to-acrn-usage))

### SOS kernel without ACRN

Without starting the ACRN hypervisor, does the SOS kernel successfully boot.  This is required for interaction-free installation which can use a first-boot script to configure the EFI boot manager.

### EFI Boot Manager updated

When the SOS is first booted, it should add an entry to the EFI boot loader to start itself under ACRN.

### Console Guest Functional

Verify that `acrn-image-base` SOS will start `core-image-base` UOS successfully.

### Graphical Guest Functional

Verify that `acrn-image-sato` SOS will start `core-image-sato` UOS successfully.

Verify that `acrn-image-sato` SOS will start `core-image-weston` UOS successfully.

The SOS/UOS should be configured such that the SOS is on one display and the UOS is a second display.

### Graphical Performance

Verify using benchmarks (e.g. glmark terrain, Unigine Valley) that GL performance inside the UOS is within 10% of the same benchmarks in the SOS under ACRN and within 10% of the same benchmarks in the UOS image using meta-intel's kernel.
