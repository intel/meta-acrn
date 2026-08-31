require recipes-bsp/grub/grub-efi_2.04.bb

FILESEXTRAPATHS_append = ":${COREBASE}/meta/recipes-bsp/grub/files"

do_mkimage() {
    cd ${B}
    # Search for the grub.cfg on the local boot media by using the
    # built in cfg file provided via this recipe
    grub-mkimage -p ${EFIDIR} -d ./grub-core/ \
                   -O ${GRUB_TARGET}-efi -o ./${GRUB_IMAGE_PREFIX}${GRUB_IMAGE} \
            boot efifwsetup efi_gop efinet efi_uga lsefimmap lsefi lsefisystab \
            exfat fat multiboot2 multiboot terminal part_msdos part_gpt normal \
            all_video aout configfile echo file fixvideo fshelp gfxterm gfxmenu \
            gfxterm_background gfxterm_menu legacycfg video_bochs video_cirrus \
            video_colors video_fb videoinfo video net tftp

}
SECURITY_CFLAGS = ""
