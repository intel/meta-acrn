require acrn-kernel_5.15.inc

SRC_URI:append = "  file://service-vm_5.15.scc"

LINUX_VERSION_EXTENSION = "-acrn-kernel-service-vm"

SUMMARY = "ACRN Kernel (Service VM)"

KERNEL_FEATURES:append = " service-vm_5.15.scc "

# config warning:  'CONFIG_IRQ_REMAP' last val (y) and .config val (n) do not matchs
# It is because, CONFIG_IRQ_REMAP depends upon IOMMU_SUPPORT, which we are explicitly disabling by setting to 'n'
# So, to suppress this warning, set
KCONF_AUDIT_LEVEL = "0"
