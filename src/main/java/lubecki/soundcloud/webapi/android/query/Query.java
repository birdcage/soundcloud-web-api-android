package lubecki.soundcloud.webapi.android.query;

import java.util.HashMap;

public abstract class Query {

  protected int limit = 50;
  protected int offset = 50;

  public abstract HashMap<String, String> createMap();
}
