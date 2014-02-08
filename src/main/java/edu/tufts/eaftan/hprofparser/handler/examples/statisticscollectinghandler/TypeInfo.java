/*
 * Copyright 2014 Edward Aftandilian. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.tufts.eaftan.hprofparser.handler.examples.statisticscollectinghandler;

/**
 * Simple data object that records basic information about a type.
 */
public abstract class TypeInfo {
  
  public String className = null;
  public long instanceCount = 0;
    
  /**
   * Returns the cumulative size in bytes of all objects of this type.
   */
  public abstract long totalSize();
  
  @Override
  public String toString() {
    return String.format("%s: %d bytes, %d instances", 
        className, totalSize(), instanceCount);
  }
}
