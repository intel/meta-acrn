inherit image-acrn

do_configure:append(){
    bb.build.exec_func('update_acrn_efi_cfg', d)
}

python update_acrn_efi_cfg() {
    cfile = d.getVar('GRUB_CFG')
    if not cfile:
        bb.fatal('Unable to read GRUB_CFG')

    root = d.getVar('GRUB_ROOT')
    if not root:
        bb.fatal('GRUB_ROOT not defined')

    # read configuration generate by oe
    try:
         cfg = open(cfile, 'rt').read().splitlines()
    except OSError:
        bb.fatal('Unable to open %s' % cfile)

    # change default boot option
    for l in range(len(cfg)):
        if cfg[l].startswith("default="):
            cfg[l] = "default='ACRN (Yocto)'"
            break

    localdata = d.createCopy()
    cfg.append("\nmenuentry 'ACRN (Yocto)'{\n")

    cfg.append("multiboot2 /acrn.bin %s %s \n" % ( \
        replace_rootfs_uuid(d, root), \
        replace_rootfs_uuid(d, localdata.getVar('APPEND')) ) )

    boot_confs = localdata.getVar("ACRN_EFI_BOOT_CONF").split(";")
    for boot_conf in boot_confs:
        if not boot_conf:
             continue
        conf = boot_conf.split(":")
        if len(conf) == 2:
            cfg.append("module2 /%s %s\n" %(conf[0] ,conf[1]))
        elif len(conf) == 3:
            cfg.append("module2 /%s %s %s\n" %(conf[0] ,conf[1] ,conf[2]))
        else:
            bb.error("unable to parse ACRN_EFI_BOOT_CONF, in \"%s\" exiting" % boot_conf )

    cfg.append('\n}\n')

    open(cfile,'w+').write('\n'.join(cfg))
}
