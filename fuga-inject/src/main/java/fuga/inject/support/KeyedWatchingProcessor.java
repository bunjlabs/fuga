package fuga.inject.support;

import fuga.inject.KeyedWatching;

interface KeyedWatchingProcessor {

    <T> boolean process(KeyedWatching<T> watching);
}
