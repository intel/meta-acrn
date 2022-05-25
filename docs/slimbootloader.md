# Boot ACRN Hypervisor with Slim Bootloader
ACRN Hypervisor can boot with Slim Bootloader. This page shows a workflow to generate the multiboot compliant container image to boot ACRN Hypervisor in the hybrid scenario with Slim Bootloader.

### Build Requirements
* openembedded-core, branch master
* meta-intel, branch master
* meta-acrn, branch master

### 1. Setup project
```
$ git clone https://git.yoctoproject.org/git/poky
$ git clone https://git.yoctoproject.org/git/meta-intel
$ git clone https://git.openembedded.org/meta-openembedded
$ git clone https://github.com/intel/meta-acrn.git
$ source poky/oe-init-build-env
build $ bitbake-layers add-layer \
../meta-intel \
../meta-acrn \
../meta-openembedded/meta-oe
```

### 2. Configure local conf
```
build $ echo "MACHINE = \"intel-corei7-64\"" >> conf/local.conf
build $ echo "BBMULTICONFIG = \"sos\"" >> conf/local.conf
```

### 3. Create SOS config file as below
```
build $ mkdir conf/multiconfig
build $ cat conf/multiconfig/sos.conf
TMPDIR = "${TOPDIR}/master-acrn-sos"
DISTRO = "acrn-demo-sos"

PREFERRED_PROVIDER_virtual/kernel = "linux-intel-acrn-sos"
PREFERRED_VERSION_linux-intel-acrn-sos = "5.10%"

ACRN_BOARD = "ehl-crb-b"
ACRN_SCENARIO = "hybrid"

IMAGE_CLASSES:append = " acrn-sblimage"
MB_ACRN_MODULES = "\
    ${TOPDIR}/conf/zephyr.txt;${TOPDIR}/conf/zephyr.bin \
    ${TOPDIR}/conf/linux.txt;${TMPDIR}/deploy/images/${MACHINE}/${KERNEL_IMAGETYPE} \
"
```
Appending the acrn-sblimage class will run a bitbake task to generate the container image including the module files specified in the MB_ACRN_MODULES list.
Note: You should not set acrn-sblimage in loca.conf because it will cause a build error when you build Yocto UOS.

### 4. Create module tag files
```
build$ echo Linux_bzImage > conf/linux.txt
build$ echo Zephyr_RawImage > conf/zephyr.txt
```

### 5. Copy Zephyr image file to the conf/zephyr.bin
Please refer to [Using Zephyr as User OS](https://projectacrn.github.io/2.7/tutorials/using_zephyr_as_user_vm.html) about how to build Zephyr

### 6. Generate SBL sign key file
```
build$ python $(SBL_ROOT)/BootloaderCorePkg/Tools/GenerateKeys.py -k cert
build$ cp cert/OS1_TestKey_Priv_RSA2048.pem cert/TestSigningPrivateKey.pem
```

Please refer to [SBL Keys Generation](https://slimbootloader.github.io/getting-started/build-host-setup.html#sbl-keys) for the details of SBL tools.

### 7. Build SOS image
```
build$ bitbake mc:sos:acrn-image-minimal
```

### 8. Check the sbl_os container image file has been deployed in the generated wic image
```
build$ sudo mount \
`sudo losetup -f -P --show master-acrn-sos/deploy/images/intel-corei7-64/acrn-image-minimal-intel-corei7-64.wic`p2 \
/mnt
build$ ls /mnt/boot/
bzImage bzImage-5.10.*-linux-intel-acrn-sos EFI loader sbl_os
```

### Optional Other Variables
* MB_DEPENDENCY  
  Set any tasks you want to run before the acrn_sblimage task start. Normally it is not needed since the acrn_sblimage is scheduled at the very last minute of the SOS image creation.

* MB_MC_DEPENDENCY  
  Set any tasks of UOS you want to run before the acrn_sblimage task start. Typically, it can be used to wait the UOS Linux kernel build in case you want to add the UOS Linux kernel bzImage to the container image so that it can boot as the pre-launched OS.  
  ```
  MB_MC_DEPENDENCY = "\
      uos:virtual/kernel:do_deply \
  "
  ```
* MB_ACRN_BINARY  
  Set the path to the ACRN Hypervisor binary file. Normally you don't need to change it from default.

* MB_ACRN_CMDLINE  
  Set the file path which includes the ACRN Hypervisor command line. 

* BASE_SBLIMAGE  
  Set the file name of the generated container image. Default is "sbl_os".

* PREGENERATED_SIGNING_KEY_SLIMBOOT_KEY_SHA256  
  Set the path to the image signing key. Default is "${TOPDIR}/cert/TestSigningPrivateKey.pem"

* BB_CURRENT_MC
  Get the Current Configuration using this variable when multiple configuration have specified in conf/local.conf using BBMULTICONFIG variable.
  Example: currentMultibootConfig = d.getVar('BB_CURRENT_MC')

* ACRN_FIRMWARE
  Set ACRN_FIRMWARE="uefi" in your sos multiconfig file (Example: conf/multiconfig/sos.conf) to generate the ACRN EFI application which allows to boot your sbl_os image on UEFI-BIOS systems.
  See the "Enable ACRN Secure Boot With EFI-Stub" page on the "Project ACRN Documentation" for the details.
  The acrn.efi file will be generated in the deployment directory (Example: master-acrn-sos/deploy/images/intel-corei7-64/acrn.efi)
