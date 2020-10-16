IMAGE_FSTYPES += "ext4 wic"

# in format of
# <kernel image>:<VMx kern_mod>:<bootarg>;
# for each module, split each module with semicolon.
# below example show zephyr.bin as VM0 without bootargs and
# bzImage as VM1 with bootargs eg :
# ACRN_EFI_BOOT_CONF ?= "zephyr.bin:Zephyr_RawImage;bzImage:Linux_bzImage:rootwait root=/dev/sda1;"
ACRN_EFI_BOOT_COMMON_CONF ?= "${KERNEL_IMAGETYPE}:Linux_bzImage;"
ACRN_EFI_BOOT_DMVERITY_CONF ?= "${KERNEL_IMAGETYPE}-${INITRAMFS_LINK_NAME}.bin:Linux_bzImage;"


ACRN_EFI_BOOT_CONF ?= "${@bb.utils.contains_any("IMAGE_CLASSES", "dm-verity-img", "${ACRN_EFI_BOOT_DMVERITY_CONF}", "${ACRN_EFI_BOOT_COMMON_CONF}", d)}"

WICVARS_append = " ACRN_EFI_BOOT_CONF IMAGE_EFI_BOOT_FILES "
