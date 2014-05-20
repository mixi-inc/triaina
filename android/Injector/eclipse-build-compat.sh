#!/bin/sh

# Execute this shell script only for Eclipse build
# DO NOT execute for gradle build !!!


MODULE_NAME="triaina/android/Injector"
LIBS_DIR="libs"
ECLIPSE_CUSTOM_LIBS_DIR="eclipse-libs"


# Move all libs under custom eclipse libs to default libs for ANT build
for file in `ls $ECLIPSE_CUSTOM_LIBS_DIR`
do
     cp $ECLIPSE_CUSTOM_LIBS_DIR/$file $LIBS_DIR
     echo "$file has been re-located for eclipse build of module ==>  $MODULE_NAME"
     echo "DO NOT commit this change to the GIT repository"
done
