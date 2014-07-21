package io.github.thred.tinyconsole.util.cron;

import java.util.ArrayList;
import java.util.List;

class CronListField extends AbstractCronField
{

    private final List<CronField> fields = new ArrayList<>();

    public CronListField(CronFieldType type)
    {
        super(type);
    }

    public void add(CronField field)
    {
        fields.add(field);
    }

    @Override
    public boolean isAllowed(CronTime time)
    {
        for (CronField field : fields)
        {
            if (field.isAllowed(time))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        
        for (CronField field : fields) {
            if (builder.length() > 0) {
                builder.append(",");
            }
            
            builder.append(field);
        }
        
        return builder.toString();
    }

}
