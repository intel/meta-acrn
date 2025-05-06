# This class:
#   Generate sample grub.cfg
#   Generate initial grub configuration grub.init.cfg
#   Generte gpg secure keys
#   Sign acrn.bin, grub.init.cfg, grub.cfg and bzImage with gpg private key
#   Build standalone grub efi having all necessary modules, signed grub.init.cfg and public gpg key
#   Sign grub bootx64efi with UEFI secure key (db.key) and Ceritificat (db.crt)
#   Verify signed grub bootx64.efi image
#

UEFI_ACRN_GRUB_MODULES = "all_video archelp boot bufio configfile crypto echo efi_gop efi_uga ext2 extcmd fat font fshelp gcry_dsa gcry_rsa gcry_sha1 gcry_sha512 gettext gfxterm linux ls memdisk minicmd mmap mpi normal part_gpt part_msdos password_pbkdf2 pbkdf2 reboot relocator search search_fs_file search_fs_uuid search_label sleep tar terminal verifiers video_fb"


PREGENERATED_ACRN_SIGNING_KEY_DIR  ?= "${TOPDIR}/keys"
GRUB_GPG_HOME = "${B}/gpghome"
GRUB_LIBDIR_x86-64 = "x86_64-efi"
BOOT_PARTITION_FSUUID ?= "1234ABCD"

python(){
        d.appendVarFlag('do_acrn_sign', 'depends', ' sbsigntool-native:do_populate_sysroot')
        d.appendVarFlag('do_acrn_sign', 'depends', ' gnupg-native:do_populate_sysroot')
        d.appendVarFlag('do_acrn_sign', 'depends', ' grub-native:do_populate_sysroot grub:do_populate_sysroot grub-efi:do_populate_sysroot grub-bootconf:do_populate_sysroot')
}

python acrn_gen_grub_cfg () {

    # Generate sample grub.cfg for secure boot
    grubefi_conf = ""
    grubefi_conf += "serial --unit=0 --speed=115200 --word=8 --parity=no --stop=1\n"
    grubefi_conf += "default='ACRN (Yocto)'\n"
    grubefi_conf += "timeout=5\n"
    grubefi_conf += "menuentry 'boot'{\n"

    kernel = d.getVar("KERNEL_IMAGETYPE")
    if d.getVar("INITRAMFS_IMAGE_BUNDLE") == "1":
        if d.getVar("INITRAMFS_IMAGE"):
            kernel = "%s-%s.bin" % \
            (d.getVar("KERNEL_IMAGETYPE"), d.getVar("INITRAMFS_LINK_NAME"))

    rootpartuuid = d.getVar("DISK_SIGNATURE_UUID")
    label_conf = "root=PARTUUID=%s" % rootpartuuid

    appendvar = d.getVar("APPEND")
    bb.note('Append: %s' % appendvar)

    grubefi_conf += "linux /%s %s rootfstype=ext4 %s\n" \
        % (kernel, label_conf, appendvar)

    initrd = "microcode.cpio"
    if initrd:
        initrds = initrd.split(';')
        grubefi_conf += "initrd"
        for rd in initrds:
            grubefi_conf += " /%s" % rd
        grubefi_conf += "\n"

    grubefi_conf += "}\n"
    grubefi_conf += "menuentry 'ACRN (Yocto)'{\n"
    grubefi_conf += "multiboot2 /acrn.bin %s rootfstype=ext4 %s \n" % \
                (label_conf, appendvar)

    acrn_efi_bootvars = d.getVar("ACRN_EFI_BOOT_CONF")
    bb.note('acrn_efi_bootvars: %s' % acrn_efi_bootvars)
    if acrn_efi_bootvars is not None:
        boot_confs = acrn_efi_bootvars.split(";")
        for boot_conf in boot_confs:
            if not boot_conf:
                continue
            conf = boot_conf.split(":")
            if len(conf) == 2:
                grubefi_conf += "module2 /%s %s\n" %(conf[0] ,conf[1])
            elif len(conf) == 3:
                grubefi_conf += "module2 /%s %s %s\n" %(conf[0] ,conf[1] ,conf[2])
            else:
                bb.error("unable to parse ACRN_EFI_BOOT_CONF, in \"%s\" exiting" \
                        % boot_conf )

        grubefi_conf += "}\n"

    cr_workdir = d.getVar("DEPLOY_DIR_IMAGE")
    bb.note("Writing grubefi config %s/grub.cfg",
                     cr_workdir)
    cfg = open("%s/grub.cfg" % cr_workdir, "w")
    cfg.write(grubefi_conf)
    cfg.close()
}

acrn_gpg_uefi_sign(){

    # Generate grub.init.cfg
    ESP_UUID=`echo ${BOOT_PARTITION_FSUUID} | sed -e 's/./&-/4'`
    cat <<-EOF >${DEPLOY_DIR_IMAGE}/grub.init.cfg
set check_signatures=enforce
export check_signatures

search --no-floppy --fs-uuid --set=root ${ESP_UUID}
configfile /efi/boot/grub.cfg
echo /efi/boot/grub.cfg did not boot the system, going to grub in 10 seconds.
EOF

    # Generate gpg keys and export public key
    gpg_root="$(mktemp -d)"
    gpg_home="${gpg_root}/link"
    ln -s "${GRUB_GPG_HOME}" "${gpg_home}"

    gpg --no-permission-warning --batch --yes --disable-dirmngr --homedir "${gpg_home}" --passphrase '' --quick-generate-key grub-signature
    gpg --no-permission-warning --batch --yes --disable-dirmngr --homedir "${gpg_home}" --passphrase '' --export -o ${PREGENERATED_ACRN_SIGNING_KEY_DIR}/grub.pub grub-signature

    rm -rf ${DEPLOY_DIR_IMAGE}/*.sig

    # todo: add checks for target files

    # sign grub.init.cfg, grub.cfg, acrn.bin and bzImage
    for i in `find ${DEPLOY_DIR_IMAGE}/ -name acrn.bin -o -name ${KERNEL_IMAGETYPE} -o -name grub.init.cfg -o -name grub.cfg`; do

        varlink=`readlink -f "$i"`

        gpg --no-permission-warning --batch --yes \
            --disable-dirmngr --homedir "${gpg_home}" --passphrase '' --detach-sign -u grub-signature $varlink

        if [ -L "$i" ]
        then
            `lnr "$varlink".sig "$i".sig`
        fi

    done
    rm -rf "${gpg_root}"


    # Build standalone grub boox64.efi binariy
    grub-mkstandalone --directory "${STAGING_LIBDIR}/grub/${GRUB_LIBDIR}" \
        --format x86_64-efi  \
        --modules "${UEFI_ACRN_GRUB_MODULES}"  \
        --pubkey ${PREGENERATED_ACRN_SIGNING_KEY_DIR}/grub.pub \
        --output ${DEPLOY_DIR_IMAGE}/grub-efi-bootx64.efi  \
        "boot/grub/grub.cfg=${DEPLOY_DIR_IMAGE}/grub.init.cfg" \
        "boot/grub/grub.cfg.sig=${DEPLOY_DIR_IMAGE}/grub.init.cfg.sig"

    # todo: add check for uefi secure keys

    # UEFI sbsign grub bootx64.efi image
    sbsign --key ${PREGENERATED_ACRN_SIGNING_KEY_DIR}/db.key \
        --cert  ${PREGENERATED_ACRN_SIGNING_KEY_DIR}/db.crt \
        --output ${DEPLOY_DIR_IMAGE}/grub-efi-bootx64.efi \
        ${DEPLOY_DIR_IMAGE}/grub-efi-bootx64.efi

    # Verify signed grub image
    sbverify --cert  ${PREGENERATED_ACRN_SIGNING_KEY_DIR}/db.crt ${DEPLOY_DIR_IMAGE}/grub-efi-bootx64.efi
}


python fakeroot do_acrn_sign() {
  bb.build.exec_func('acrn_gen_grub_cfg', d)
  bb.build.exec_func('acrn_gpg_uefi_sign', d)
}


acrn_gen_grub_cfg[vardeps] += "APPEND ACRN_EFI_BOOT_CONF BOOT_PARTITION_FSUUID DISK_SIGNATURE_UUID INITRD_LIVE KERNEL_IMAGETYPE IMAGE_LINK_NAME"

addtask acrn_sign after do_rootfs before do_image

do_acrn_sign[fakeroot] = "1"
do_acrn_sign[cleandirs] += "${GRUB_GPG_HOME} "
