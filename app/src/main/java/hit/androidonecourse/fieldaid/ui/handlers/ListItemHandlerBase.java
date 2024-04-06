package hit.androidonecourse.fieldaid.ui.handlers;

import android.content.Context;

import hit.androidonecourse.fieldaid.domain.RepositoryMediator;

public abstract class ListItemHandlerBase {
    protected Context context;
    protected RepositoryMediator repositoryMediator;

    public ListItemHandlerBase(Context context) {
        this.context = context;
        repositoryMediator = RepositoryMediator.getInstance(context);
    }
}
