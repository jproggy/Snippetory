/// Copyright JProggy
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.

package org.jproggy.snippetory.sql.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.jproggy.snippetory.engine.DataSink;
import org.jproggy.snippetory.engine.DataSinks;
import org.jproggy.snippetory.engine.Location;

public class SqlSinks extends DataSinks implements StatementBinder {

  public SqlSinks(List<DataSink> parts, Location placeHolder) {
    super(parts, placeHolder);
  }

  protected SqlSinks(DataSinks template, Location parent) {
    super(template, parent);
  }

  @Override
  public DataSinks cleanCopy(Location parent) {
    return new SqlSinks(this, parent);
  }

  @Override
  public int bindTo(PreparedStatement stmt, int offset) throws SQLException {
    for (DataSink v : parts) {
      offset = ((StatementBinder)v).bindTo(stmt, offset);
    }
    return offset;
  }

}
