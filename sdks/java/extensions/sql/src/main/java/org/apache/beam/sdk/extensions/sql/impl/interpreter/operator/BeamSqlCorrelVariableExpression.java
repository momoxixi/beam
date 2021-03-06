/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.beam.sdk.extensions.sql.impl.interpreter.operator;

import static com.google.common.base.Preconditions.checkState;

import com.google.common.collect.ImmutableMap;
import org.apache.beam.sdk.transforms.windowing.BoundedWindow;
import org.apache.beam.sdk.values.Row;
import org.apache.calcite.sql.type.SqlTypeName;

/** A primitive operation for dereferencing a correlation variable. */
public class BeamSqlCorrelVariableExpression extends BeamSqlExpression {

  private final int correlationId;

  public BeamSqlCorrelVariableExpression(SqlTypeName sqlTypeName, int correlationId) {
    super(null, sqlTypeName);
    this.correlationId = correlationId;
  }

  @Override
  public boolean accept() {
    return true;
  }

  @Override
  public BeamSqlPrimitive evaluate(
      Row inputRow, BoundedWindow window, ImmutableMap<Integer, Object> correlateEnv) {

    Object correlateValue = correlateEnv.get(correlationId);

    checkState(
        correlateValue != null,
        "Correlation variables %s not found in environment %s",
        correlationId,
        correlateEnv);

    return BeamSqlPrimitive.of(getOutputType(), correlateValue);
  }
}
