# References

## ACRN Overview

* [Introduction to ACRN](https://projectacrn.github.io/2.7/introduction/index.html). Good overview.
* [Virtio Supported Devices](https://projectacrn.github.io/2.7/developer-guides/hld/hld-virtio-devices.html#supported-virtio-devices). Some of these virtio devices are *not* upstream yet, and are only in Production Kernel.
* [Configuring ACRN's memory use](https://projectacrn.github.io/2.7/faq.html#how-do-i-configure-acrn-s-memory-use). I hope one day this isn't required. Currently we don't expose a nice way to control this in the layer (see #12).

## GVT

* [Enable GVT-d in ACRN](https://projectacrn.github.io/2.7/tutorials/gpu-passthru.html).
* [GVT Kernel Options](https://projectacrn.github.io/2.7/user-guides/kernel-parameters.html#gvt-g-acrngt-kernel-options-details)
* [Mesa Environment Variables](https://www.mesa3d.org/envvars.html). Useful to get FPS or force software paths for benchmarking.

## Other

* [USB passthrough](https://projectacrn.github.io/2.7/developer-guides/hld/usb-virt-hld.html#usb-host-virtualization)

## Kernels

* [Intel Production Kernel](https://github.com/intel/linux-intel-lts), as built in `meta-intel` by `linux-intel*.bb`. This is what `meta-acrn` builds.
