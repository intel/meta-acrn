# References 

## ACRN Overview

* [Introduction to ACRN](https://projectacrn.github.io/latest/introduction/index.html). Good overview.
* [Virtio Supported Devices](https://projectacrn.github.io/latest/developer-guides/hld/hld-virtio-devices.html#supported-virtio-devices). Some of these virtio devices are *not* upstream yet, and are only in Production Kernel.
* [Configuring ACRN's memory use](https://projectacrn.github.io/latest/faq.html#how-do-i-configure-acrn-s-memory-use). I hope one day this isn't required. Currently we don't expose a nice way to control this in the layer (see #12).

## GVT

* [ACRN High-level Graphics Virtualisation Technology (GVT) Design](https://projectacrn.github.io/latest/developer-guides/hld/hld-APL_GVT-g.html).  Useful detailed overview of the GVT-* variations.
* [GVT Kernel Options](https://projectacrn.github.io/latest/developer-guides/GVT-g-kernel-options.html)
* [GPU Passthrough on Sky Lake](https://projectacrn.github.io/latest/tutorials/skl-nuc.html).  This is GVT-d so true passthrough, the SOS can't use the GPU.  Of limited use and the patches are obsolete but still useful to understand.
* [Running AGL as VMs](https://projectacrn.github.io/latest/tutorials/agl-vms.html).  This is basically the ACRN + Automotive Grade Linux demo, where the SOS and two UOSs are on three separate screens.
* [Mesa Environment Variables](https://www.mesa3d.org/envvars.html). Useful to get FPS or force software paths for benchmarking.

## Other

* [USB passthrough](https://projectacrn.github.io/latest/developer-guides/hld/usb-virt-hld.html#usb-host-virtualization)

## Kernels

* [acrn-kernel](https://github.com/projectacrn/acrn-kernel). This is the active ACRN kernel development tree so too unstable to use.
* [Intel Production Kernel](https://github.com/intel/linux-intel-lts/tree/4.19/base), as built in `meta-intel` by `linux-intel.bb`. This is what `meta-acrn` builds, and once #9 and #10 are resolved we can simply use `linux-intel.bb` with extra config fragments.
* [linux-iot-lts2018](https://github.com/clearlinux-pkgs/linux-iot-lts2018) is the Clear Linux packaging for Production Kernel, following PK updates with a IoT-specific configuration that is *mostly* from `acrn-kernel` but does have changes. Worth keeping an eye on.
