#! /bin/sh
# Copyright (C) 2019 Intel
# MIT licensed

# TODO: lots of hardcoded values in here.  Need a generic solution.

set -e

# Prune previous ACRN boot entries
for boot in $(efibootmgr | perl -n -e '/Boot([0-9a-fA-F]+).*ACRN.*/ && print "$1\n"'); do
    efibootmgr -b $boot -B
done

efibootmgr -c -l "\EFI\BOOT\acrn.efi" \
    -L "ACRN (Yocto)" \
    -u "bootloader=\EFI\BOOT\bootx64.efi"
