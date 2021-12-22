require linux-intel-acrn_5.10.inc

SRC_URI:append = " file://sos_5.10.scc"

LINUX_VERSION_EXTENSION = "-linux-intel-acrn-sos"

SUMMARY = "Linux Kernel with ACRN enabled (SOS)"

KERNEL_FEATURES:append = " features/criu/criu-enable.scc \
                          cgl/cfg/iscsi.scc \
                          sos_5.10.scc \
"

# config warning:  'CONFIG_IRQ_REMAP' last val (y) and .config val (n) do not matchs
# It is because, CONFIG_IRQ_REMAP depends upon IOMMU_SUPPORT, which we are explicitly disabling by setting to 'n'
# So, to suppress this warning, set
KCONF_AUDIT_LEVEL = "0"
