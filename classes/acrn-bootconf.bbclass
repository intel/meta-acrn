def get_acrn_efi_boot_conf(d):
  vmflags = d.getVar("VMFLAGS")

  bootconf = ""
  for flag in vmflags.split():
    append = d.getVar("VM_APPEND_%s" % flag) or ""
    kernelimage = d.getVar("KERNEL_IMAGE_%s" % flag) or ""
    kernelmod = d.getVar("KERNEL_MOD_%s" % flag) or ""
    acpibin = d.getVar("ACPI_BIN_%s" % flag) or ""
    acpitag = d.getVar("ACPI_TAG_%s" % flag) or ""

    if kernelimage == "" or kernelmod == "":
      bb.warn("KERNEL_IMAGE_{s} or KERNEL_MOD_{s} set to blank, this might cause error to boot VM({s}).".format(s = flag))

    bootconf += "%s:%s:%s;" % (kernelimage, kernelmod, append)
    if not acpibin == "" and not acpitag == "":
      bootconf += "%s:%s;" % (acpibin, acpitag)

  return bootconf

# list of pre-launched vm including Service vm
VMFLAGS ??= " vm0 "

# default value for vm0 based on industry scenario for nuc7i7dnb
VM_APPEND_vm0 ??= "${APPEND}"
KERNEL_IMAGE_vm0 ??= "${KERNEL_IMAGETYPE}"
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
