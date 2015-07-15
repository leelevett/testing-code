#!/usr/bin/env bash

setUp()
{
  MAIN_DIR=../../main
  PATH=$MAIN_DIR/bin:$PATH

  cp ./mock-curl.sh $MAIN_DIR/bin/curl
}

tearDown()
{
  rm $MAIN_DIR/bin/curl

  unset prop1
  unset prop2
}

testPropertyService()
{
  cd $MAIN_DIR/bin && \
  source read-props.sh && \
  cd - &> /dev/null

  assertEquals "prop1 is not set correctly" "a property" "$prop1"
  assertEquals "prop2 is not set correctly" "A second property!" "$prop2"
}

. ../lib/shunit2-2.1.6/src/shunit2