#!/usr/bin/env bash

suite ()
{
    suite_addTest ./prop-service-test.sh
}

. ../lib/shunit2-2.1.6/src/shunit2