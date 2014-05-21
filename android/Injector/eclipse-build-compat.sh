#!/bin/bash

# Execute this shell script only for Eclipse build
# DO NOT execute for gradle build !!!


MODULE_NAME="triaina/android/Injector"
LIBS_DIR="libs"
ECLIPSE_CUSTOM_LIBS_DIR="eclipse-libs"


# Move all libs under custom eclipse libs to default libs for ANT build
if [ -d ${ECLIPSE_CUSTOM_LIBS_DIR} ]; then
    for file in `ls $ECLIPSE_CUSTOM_LIBS_DIR`
    do
        if [ -d ${LIBS_DIR} ]; then
            cp $ECLIPSE_CUSTOM_LIBS_DIR/$file $LIBS_DIR
            echo "$file has been re-located for eclipse build of module ==>  $MODULE_NAME"
            echo "DO NOT commit this change to the GIT repository"
        else
            echo "Destination directory \"$LIBS_DIR\" missing. Please check your project structure"
        fi
    done
else
    echo "Source directory \"$ECLIPSE_CUSTOM_LIBS_DIR\" missing. Please check your project structure"
fi
