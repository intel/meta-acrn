SUMMARY = "A Type 1 hypervisor stack, running directly on the bare-metal hardware"
HOMEPAGE = "https://projectacrn.org/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ba07c9571f2096ec6349ef0167ec5e60"

SRC_URI = "git://github.com/projectacrn/acrn-hypervisor.git;protocol=https;branch=${SRCBRANCH}; \
        file://0001-acrn-Use-std-gnu11-to-fix-build-with-GCC-15.patch \
        "
# Snapshot tags are of the format:
# acrn-<year>w<week>.<day>-<timestamp><pass|fail>
PV = "3.3"
SRCREV = "d68f223dde407a70df9f66384796ac15725addfd"
SRCBRANCH = "release_3.3"

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

CVE_STATUS[CVE-2019-18844] = "fixed-version: Fixed from version 1.2+"
CVE_STATUS[CVE-2021-36145] = "fixed-version: Fixed from version 2.5+"

# Skip TMPDIR [buildpaths] QA check
ERROR_QA:remove = "buildpaths"
