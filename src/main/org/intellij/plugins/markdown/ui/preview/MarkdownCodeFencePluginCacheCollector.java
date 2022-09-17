package org.intellij.plugins.markdown.ui.preview;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.Objects;

public class MarkdownCodeFencePluginCacheCollector {
  @NotNull private final VirtualFile myFile;
  @NotNull private final Collection<File> myAliveCachedFiles = ContainerUtil.newHashSet();

  public MarkdownCodeFencePluginCacheCollector(@NotNull VirtualFile file) {
    myFile = file;
  }

  @NotNull
  public Collection<File> getAliveCachedFiles() {
    return myAliveCachedFiles;
  }

  @NotNull
  public VirtualFile getFile() {
    return myFile;
  }

  public void addAliveCachedFile(@NotNull File file) {
    myAliveCachedFiles.add(file);
  }

  //need to override `equals()`/`hasCode()` to scan cache for the latest `cacheProvider` only, see 'ZenUmlCodeFencePluginCache.registerCacheProvider()'
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MarkdownCodeFencePluginCacheCollector collector = (MarkdownCodeFencePluginCacheCollector)o;
    return Objects.equals(myFile, collector.myFile);
  }

  @Override
  public int hashCode() {
    return Objects.hash(myFile);
  }
}