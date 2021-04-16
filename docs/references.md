# References

## ACRN Overview

* [Introduction to ACRN](https://projectacrn.github.io/2.4/introduction/index.html). Good overview.
* [Virtio Supported Devices](https://projectacrn.github.io/2.4/developer-guides/hld/hld-virtio-devices.html#supported-virtio-devices). Some of these virtio devices are *not* upstream yet, and are only in Production Kernel.
* [Configuring ACRN's memory use](https://projectacrn.github.io/2.4/faq.html#how-do-i-configure-acrn-s-memory-use). I hope one day this isn't required. Currently we don't expose a nice way to control this in the layer (see #12).

## GVT

* [ACRN High-level Graphics Virtualisation Technology (GVT) Design](https://projectacrn.github.io/2.4/developer-guides/hld/hld-APL_GVT-g.html#gvt-g-high-level-design).  Useful detailed overview of the GVT-* variations.
* [GVT Kernel Options](https://projectacrn.github.io/2.4/user-guides/kernel-parameters.html#gvt-g-acrngt-kernel-options-details)
* [Mesa Environment Variables](https://www.mesa3d.org/envvars.html). Useful to get FPS or force software paths for benchmarking.

## Other

* [USB passthrough](https://projectacrn.github.io/2.4/developer-guides/hld/usb-virt-hld.html#usb-host-virtualization)

## Kernels

* [Intel Production Kernel](https://github.com/intel/linux-intel-lts), as built in `meta-intel` by `linux-intel.bb`. This is what `meta-acrn` builds.
