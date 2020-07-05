package common_test


import (
	"github.com/OSAlt/geekbeacon/service/common"
	"github.com/stretchr/testify/assert"
	"testing"
)

func TestPagination(t *testing.T) {
	obj := common.Pagination{
		PageNumber: 1,
		PageSize:   50,
		Limit:      0,
	}

	offset, err := obj.GetOffset()
	assert.NotNil(t, err)
	assert.Equal(t, offset, 0 )
	obj.PageNumber = 2
	offset, err = obj.GetOffset()
	assert.NotNil(t, err)
	assert.Equal(t, offset, 51 )

}
