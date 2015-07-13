#!/usr/bin/env bash

setUp()
{
  MAIN_DIR=../../main
  PATH=$MAIN_DIR/bin:$PATH
}

tearDown()
{
  echo "tearing down"
}

testPropertyService()
{
  cd $MAIN_DIR/bin && \
  source read-props.sh && \
  cd - &> /dev/null

  echo "$prop1:$prop2"

  assertEquals "prop1 is not set correctly" "a property" "$prop1"
  assertEquals "prop2 is not set correctly" "A second property!" "$prop2"
}

. ../lib/shunit2-2.1.6/src/shunit2