require linux-intel-acrn_6.1.inc

SRC_URI:append = " file://service-vm_6.1.scc"

LINUX_VERSION_EXTENSION = "-linux-intel-acrn-service-vm"

SUMMARY = "Linux Kernel with ACRN enabled (Service VM)"

KERNEL_FEATURES:append = " features/criu/criu-enable.scc \
                          cgl/cfg/iscsi.scc \
                          service-vm_6.1.scc \
"

# config warning:  'CONFIG_IRQ_REMAP' last val (y) and .config val (n) do not matchs
# It is because, CONFIG_IRQ_REMAP depends upon IOMMU_SUPPORT, which we are explicitly disabling by setting to 'n'
# So, to suppress this warning, set
KCONF_AUDIT_LEVEL = "0"
