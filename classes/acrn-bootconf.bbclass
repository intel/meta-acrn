# Add acrn-bootconf bbclass to hold variable for boot configuration.
#
# ACRN_EFI_BOOT_CONF can be difficult to read and write, add new variable
# to allow configure boot configuration for each vm in more readable way.
#
# VMFLAGS - list of pre-launched VM including Service vm
# VM_APPEND - VM kernel commandline
# KERNEL_IMAGE - kernel image like bzImage for each vm
# KERNEL_MOD - kern_mod tag in acrn scenario xml for each
# ACPI_TAG - ACPI tag for specific VM
# ACPI_BIN - binary of ACPI tables for a specific vm
# Optional:
# PART_LABEL - Partition Label, if set, it allow grub bootloader to pick modules/kernel binaries by partition label
#
# using hybrid scenario for nuc7i7dnb as example:
#  VMFLAGS = "vm0 vm1"
#  # vm0
#  VM_APPEND_vm0 = "xxx"
#  KERNEL_IMAGE_vm0 = "zephyr.bin"
#  KERNEL_MOD_vm0 = "Zephyr_RawImage"
#  ACPI_TAG_vm0 = "ACPI_VM0"
#  ACPI_BIN_vm0 = "ACPI_VM0.bin"
#  PART_LABEL_vm0 = "boot"
#
#  # vm1
#  VM_APPEND_vm1 = "xxx"
#  KERNEL_IMAGE_vm1 = "bzImage"
#  KERNEL_MOD_vm1 = "Linux_bzImage"
#  PART_LABEL_vm1 = "boot"
#


def get_acrn_efi_boot_conf(d):
  vmflags = d.getVar("VMFLAGS")

  bootconf = ""
  for flag in vmflags.split():
    append = d.getVar("VM_APPEND_%s" % flag) or ""
    kernelimage = d.getVar("KERNEL_IMAGE_%s" % flag) or ""
    partlabel = d.getVar("PART_LABEL_%s" % flag) or ""
    kernelmod = d.getVar("KERNEL_MOD_%s" % flag) or ""
    acpibin = d.getVar("ACPI_BIN_%s" % flag) or ""
    acpitag = d.getVar("ACPI_TAG_%s" % flag) or ""

    if kernelimage == "" or kernelmod == "":
      bb.warn("KERNEL_IMAGE_{s} or KERNEL_MOD_{s} set to blank, this might cause error to boot VM({s}).".format(s = flag))

    bootconf += "%s:%s:%s:%s;" % (kernelimage, kernelmod, append, partlabel)
    if not acpibin == "" and not acpitag == "":
      bootconf += "%s:%s;" % (acpibin, acpitag)

  return bootconf

# list of pre-launched vm including Service vm
VMFLAGS ??= " vm0 "

# default value for vm0 based on industry scenario for nuc7i7dnb
VM_APPEND_vm0 ??= "${APPEND}"
KERNEL_IMAGE_vm0 ??= "/${KERNEL_IMAGETYPE}"
KERNEL_MOD_vm0 ??= "Linux_bzImage"
ACPI_TAG_vm0 ??= ""
ACPI_BIN_vm0 ??= ""

# in format of
# <kernel image>:<VMx kern_mod>:<bootarg>;
# for each module, split each module with semicolon.
# below example show zephyr.bin as VM0 without bootargs and
# bzImage as VM1 with bootargs eg :
# ACRN_EFI_BOOT_CONF ?= "zephyr.bin:Zephyr_RawImage;bzImage:Linux_bzImage:rootwait root=/dev/sda1;"
ACRN_EFI_BOOT_CONF ??= "${@get_acrn_efi_boot_conf(d)}"


# ACRN_EFI_GRUB2_MOD_CFG is semicolon (;) sperated list of auxiliary grub2 modules/commands to make
# entries right before multiboot2 acrn.bin
# Fox example:
# ACRN_EFI_GRUB2_MOD_CFG = “insmod ext2;insmod …”
#
#   menuentry 'ACRN (Yocto)'{
#    insmod ext2
#    insmod …
#    …
#    multiboot2 /acrn.bin
#    ..
#   }
ACRN_EFI_GRUB2_MOD_CFG ??= ""
ACRN_HV_EFI_CFG ??= ""
