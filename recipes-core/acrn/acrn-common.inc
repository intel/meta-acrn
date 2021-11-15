SUMMARY = "A Type 1 hypervisor stack, running directly on the bare-metal hardware"
HOMEPAGE = "https://projectacrn.org/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b762ef53db85c389256a9d215053edf7"

SRC_URI = "git://github.com/projectacrn/acrn-hypervisor.git;protocol=https;branch=${SRCBRANCH}; \
           file://0001-acrnprobe-supress-openssl-3.0-deprecated-declaration.patch \
           git://github.com/intel-innersource/virtualization.hypervisors.acrn.acrn-dev.acrn-offline-patch.git;protocol=https;branch=master;destsuffix=offline-patches;name=innersrc \
"

# Snapshot tags are of the format:
# acrn-<year>w<week>.<day>-<timestamp><pass|fail>
PV = "2.7"
SRCREV = "79a5d7a787ea8923ba9859b1f1f88ad252796d49"
SRCREV_innersrc = "91c34ec6b3628a0e4b077adf388978e1f5d358af"
SRCBRANCH = "master"

UPSTREAM_CHECK_GITTAGREGEX = "^v(?P<pver>\d+(\.\d+)+)$"

S = "${WORKDIR}/git"

CVE_PRODUCT = "acrn"

# y for release build, n for debug build.
ACRN_RELEASE ?= "n"

EXTRA_OEMAKE += "RELEASE=${ACRN_RELEASE} \
                 SYSROOT=${STAGING_DIR_TARGET} \
                 O=${B} \
                 DESTDIR=${D} \
                 prefix=${prefix} \
                 bindir=${bindir} \
                 libdir=${libdir} \
                 datadir=${datadir} \
                 includedir=${includedir} \
                 systemd_unitdir=${systemd_unitdir}"


# acrn supports build objects out-of-tree but builds must be performed from
# inside the source
B = "${WORKDIR}/build"
do_configure[cleandirs] = "${B}"
do_configure[dirs] = "${S}"
do_compile[dirs] = "${S}"
do_install[dirs] = "${S}"

# Fixed in v1.2 and onward
CVE_CHECK_WHITELIST += "CVE-2019-18844"

copy_sources() {
    cp -r -f ${WORKDIR}/offline-patches/boards/* ${S}/misc/config_tools/data/
}
do_patch[postfuncs] += "copy_sources"
