# Testing Procedure

## Test Hardware

All tests should be ran primarily on the supported platform:

* Tiger Lake NUC ([details here](https://projectacrn.github.io/2.7/reference/hardware.html#verified-platforms-according-to-acrn-usage))

### Console Guest Functional

Verify that `acrn-image-base` SOS will start `core-image-base` UOS successfully.

### Graphical Guest Functional

Verify that `acrn-image-sato` SOS will start `core-image-sato` UOS successfully.

Verify that `acrn-image-sato` SOS will start `core-image-weston` UOS successfully.

The SOS/UOS should be configured such that the SOS is on one display and the UOS is a second display.

### Graphical Performance

Verify using benchmarks (e.g. glmark terrain, Unigine Valley) that GL performance inside the UOS is within 10% of the same benchmarks in the SOS under ACRN and within 10% of the same benchmarks in the UOS image using meta-intel's kernel.
